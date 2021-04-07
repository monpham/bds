package backend.realestate.controller;

import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Comment;
import backend.realestate.repository.CommentRepository;
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
@RequestMapping("/api/comment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Comment comment) throws IOException {
        commentRepository.save(comment);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Comment>> getAll() throws IOException {
        List<Comment> comments = commentRepository.findAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy phòng ban này"));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        commentRepository.deleteById(id);
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteComment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByComment(@RequestBody Comment comment) {
        commentRepository.deleteById(comment.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Comment> comments = commentRepository.findAll();
        comments = comments.stream().filter(
                item -> item.getId().toString().contains(searchString.getSearchString())
                        || item.getLinhVuc().contains(searchString.getSearchString())
                        || item.getSoSao() == Integer.parseInt(searchString.getSearchString())
        ).collect(Collectors.toList());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
