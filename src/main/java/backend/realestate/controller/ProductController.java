package backend.realestate.controller;

import backend.realestate.dao.ElasticsearchDao;
import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Product;
import backend.realestate.repository.ProductRepository;
import backend.realestate.repository.ProjectRepository;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import backend.realestate.service.UploadToCloud;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ElasticsearchDao elasticsearchDao;

    @PostMapping("/save")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Product product) throws IOException {
        product.setImage(UploadToCloud.uploadToCloud(product.getImage()));
        productRepository.save(product);
        elasticsearchDao.save(product);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @PostMapping("/saveList")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveList(@RequestBody List<Product> products) throws IOException {
        for (Product product : products) {
            product.setImage(UploadToCloud.uploadToCloud(product.getImage()));
            productRepository.save(product);
            elasticsearchDao.save(product);

        }
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/getPage/{page}/{size}")
    public ResponseEntity<List<Product>> getPage(@PathVariable int page, @PathVariable int size) throws IOException {
        PageRequest pageable = PageRequest.of(page, size);
        List<Product> products = productRepository.findAll(pageable).toList();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAll() throws IOException {
        List<Product> products = productRepository.findAll();
        QueryBuilder query;
        query = QueryBuilders.matchAllQuery();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/getProductId/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getByProjectId/{id}")
    public ResponseEntity<?> getByProjectId(@PathVariable Long id) {
        List<Product> product = productRepository.getAllByProject_Id(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        productRepository.deleteById(id);
        elasticsearchDao.delete(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByProduct(@RequestBody Product product) {
        productRepository.deleteById(product.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Product> products = productRepository.findAll();
        products = products.stream().filter(
                item -> item.getId().toString().contains(searchString.getSearchString())
                        || item.getTenSanPham().contains(searchString.getSearchString())
                        || item.getDiaChi().contains(searchString.getSearchString())
                        || (item.getDienTich() != null && item.getDienTich().toString().contains(searchString.getSearchString()))
                        || item.getProject().getTenDuAn().contains(searchString.getSearchString())
        ).collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn2")
    public ResponseEntity<?> searchElasticsearch(@RequestBody SearchForm searchString) throws ExecutionException, InterruptedException {
        QueryBuilder query;
        if (Strings.isEmpty(searchString.getSearchString())) {
            query = QueryBuilders.matchAllQuery();
        } else {
            query = QueryBuilders.multiMatchQuery(searchString.getSearchString())
                    .field("tenSanPham", 3.0f).field("fulltext").fuzziness(1)
                    .field("diaChi", 3.0f).field("fulltext").fuzziness(1)
                    .field("giaTien", 3.0f).field("fulltext").fuzziness(1);
        }
        SearchResponse response = null;
        response = elasticsearchDao.search(query, 0, 10);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/getHints")
    public ResponseEntity<?> getHints(@RequestBody SearchForm searchString) throws ExecutionException, InterruptedException {
        try {
            List<TermSuggestion.Entry> suggestions = elasticsearchDao.getHints(0, 10, searchString.getSearchString());
            List<String> texts = new ArrayList<>();
            for (TermSuggestion.Entry entry : suggestions) {
                for (TermSuggestion.Entry.Option option : entry.getOptions()) {
                    texts.add(option.getText().string());
                }
            }
            return new ResponseEntity<>(texts, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failded", HttpStatus.OK);
    }

    @GetMapping("/getRelativeProduct/{id}")
    public ResponseEntity<?> getRelactiveProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).get();
        List<Product> products = productRepository.findAllByCreatedDateAfterOrderByCreatedDateDesc(product.getCreatedDate());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/getCountItem")
    public ResponseEntity<?> getCountItem() {
        return new ResponseEntity<>(productRepository.getItemCount(), HttpStatus.OK);
    }

    @GetMapping("/synchroniseElasticsearch")
    public ResponseEntity<?> synchroniseElasticsearch() {
//        QueryBuilder query = QueryBuilders.matchAllQuery();
//        SearchResponse response = elasticsearchDao.search(query, 0, 10);
        List<Product> products = productRepository.findAll();
        for (Product p : products
        ) {
            elasticsearchDao.save(p);
        }
        return new ResponseEntity(new ResponseMessage("Synchronise successfully"), HttpStatus.OK);
    }

    @GetMapping("/countAllByCreatedDate")
    public ResponseEntity<?> countAllByCreatedDate() throws IOException {
        List<Map<String, Object>> news = productRepository.countAllByCreatedDate();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/countProductByProject")
    public Object[] countProductByProject() {
        return productRepository.countProductByProject();
    }

    @GetMapping("/reportProductByProject")
    public Object[] reportProductByProject() {
        return productRepository.reportProductByProject();
    }
}
