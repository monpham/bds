package backend.realestate.controller;

import backend.realestate.dao.ElasticsearchDao;
import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Partner;
import backend.realestate.repository.PartnerRepository;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import backend.realestate.service.UploadToCloud;
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
@RequestMapping("/api/partner")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PartnerController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PartnerRepository partnerRepository;
    @Autowired
    ElasticsearchDao elasticsearchDao;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Partner partner) throws IOException {
        partner.setLogo(UploadToCloud.uploadToCloud(partner.getLogo()));
        partnerRepository.save(partner);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Partner>> getAll() throws IOException {
        List<Partner> partners = partnerRepository.findAll();
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Partner partner = partnerRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(partner, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        partnerRepository.deleteById(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deletePartner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByPartner(@RequestBody Partner partner) {
        partnerRepository.deleteById(partner.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Partner> partners = partnerRepository.findAll();
        partners = partners.stream().filter(
                item -> item.getId().toString().contains(searchString.getSearchString())
                        || item.getTenDoiTac().contains(searchString.getSearchString())
                        || item.getDiaChi().contains(searchString.getSearchString())
                        || item.getEmail().contains(searchString.getSearchString())
                        || item.getSdt().contains(searchString.getSearchString())
                        || item.getLinhVuc().contains(searchString.getSearchString())
        ).collect(Collectors.toList());
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }
}
