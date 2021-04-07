package backend.realestate.controller;

import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.HotPot;
import backend.realestate.repository.HotPotRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotPot")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HotPotController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    HotPotRepository hotPotRepository;

    @PostMapping("/save")
    public ResponseEntity<?> add(@Valid @RequestBody HotPot hotPot) throws IOException {
        hotPotRepository.save(hotPot);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<HotPot>> getAll() throws IOException {
        List<HotPot> hotPots = hotPotRepository.findAll();
        hotPots.removeIf(hotpot -> hotpot.getId() == 3);
        return new ResponseEntity<>(hotPots, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        HotPot hotPot = hotPotRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(hotPot, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        hotPotRepository.deleteById(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteHotPot")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByHotPot(@RequestBody HotPot hotPot) {
        hotPotRepository.deleteById(hotPot.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }


}
