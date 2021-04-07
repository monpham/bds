package backend.realestate.controller;

import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Message;
import backend.realestate.repository.MessageRepository;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Message message) throws IOException {
        messageRepository.save(message);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Message>> getAll() throws IOException {
        List<Message> messages = messageRepository.findAll();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Message message = messageRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getCountItem")
    public ResponseEntity<?> getCountItem() {
        return new ResponseEntity<>(messageRepository.getItemCount(), HttpStatus.OK);
    }

    @GetMapping("/getPage/{page}/{size}")
    public ResponseEntity<List<Message>> getPage(@PathVariable int page, @PathVariable int size) throws IOException {
        PageRequest pageable = PageRequest.of(page, size);
        List<Message> messageList = messageRepository.findAll(pageable).toList();
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        messageRepository.deleteById(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteMessage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByMessage(@RequestBody Message message) {
        messageRepository.deleteById(message.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Message> messages = messageRepository.findAll();
        messages = messages.stream().filter(
                item -> item.getId().toString().contains(searchString.getSearchString())
                        || item.getContent().contains(searchString.getSearchString())
        ).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
