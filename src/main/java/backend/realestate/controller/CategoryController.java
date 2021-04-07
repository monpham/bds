package backend.realestate.controller;

import backend.realestate.dao.ElasticsearchDao;
import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Category;
import backend.realestate.repository.CategoryRepository;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ElasticsearchDao elasticsearchDao;

    @PostMapping("/save")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Category category) throws IOException {
        categoryRepository.save(category);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Category>> getAll() throws IOException {
        List<Category> categorys = categoryRepository.findAll();
        return new ResponseEntity<>(categorys, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        categoryRepository.deleteById(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByCategory(@RequestBody Category category) {
        categoryRepository.deleteById(category.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Category> categorys = categoryRepository.findAll();
        categorys = categorys.stream().filter(
                item -> item.getId().toString().contains(searchString.getSearchString())
                        || item.getName().contains(searchString.getSearchString())
        ).collect(Collectors.toList());
        return new ResponseEntity<>(categorys, HttpStatus.OK);
    }
}
