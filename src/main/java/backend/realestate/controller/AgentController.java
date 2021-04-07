package backend.realestate.controller;

import backend.realestate.dao.ElasticsearchDao;
import backend.realestate.message.request.SearchForm;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Agent;
import backend.realestate.model.Role;
import backend.realestate.model.RoleName;
import backend.realestate.model.User;
import backend.realestate.repository.AgentRepository;
import backend.realestate.repository.ProjectRepository;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import backend.realestate.service.UploadToCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*", maxAge = 3600)
public class    AgentController {

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ElasticsearchDao elasticsearchDao;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Agent agent) throws IOException {
        User user = agent.getUser();
        Set<Role> roles = new HashSet<>();
        user.setPassword(encoder.encode(user.getPassword()));
        roles.add(roleRepository.findByName(RoleName.ROLE_PM).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find.")));
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        agent.setUser(user);
        agentRepository.save(agent);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @PostMapping("/registerAgent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@Valid @RequestBody Agent agent) throws IOException {
        User agentUser = agent.getUser();
        Set<Role> roles = new HashSet<>();
        agentUser.setPassword(encoder.encode(agentUser.getPassword()));
        roles.add(roleRepository.findByName(RoleName.ROLE_PM).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find.")));
        agentUser.setRoles(roles);
        agentUser.setImage(UploadToCloud.uploadToCloud(agentUser.getImage()));
        agent.setUser(agentUser);
        agentRepository.save(agent);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpAsAgent(@Valid @RequestBody Agent agent) throws IOException {
        if (userRepository.existsByEmail(agent.getUser().getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> email đã được đăng ký"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(agent.getUser().getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username đã được đăng ký"), HttpStatus.BAD_REQUEST);
        }
        Set<Role> roles = new HashSet<>();
        User agentUser = agent.getUser();
        agentUser.setPassword(encoder.encode(agentUser.getPassword()));
        roles.add(roleRepository.findByName(RoleName.ROLE_PM).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find.")));
        agentUser.setRoles(roles);
        agentUser.setImage(UploadToCloud.uploadToCloud(agentUser.getImage()));
        agent.setUser(agentUser);
        agentRepository.save(agent);
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @PostMapping("/saveList")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveList(@RequestBody List<Agent> agents) throws IOException {
        for (Agent agent : agents) {
            User agentUser = agent.getUser();
            agentUser.setImage(UploadToCloud.uploadToCloud(agentUser.getImage()));
            agent.setUser(agentUser);
            agentRepository.save(agent);
        }
        return new ResponseEntity<>(new ResponseMessage("Adding successfully"), HttpStatus.OK);
    }

    @GetMapping("/getPage/{page}/{size}")
    public ResponseEntity<List<Agent>> getPage(@PathVariable int page, @PathVariable int size) throws IOException {
        PageRequest pageable = PageRequest.of(page, size);
        List<Agent> agents = agentRepository.findAll(pageable).toList();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Agent>> getAll() throws IOException {
        List<Agent> agents = agentRepository.findAll();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @GetMapping("/getAgentId/{id}")
    public ResponseEntity<?> showEditForm(@PathVariable Long id) {
        Agent agent = agentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Fail! -> Không tìm thấy chuyên gia này"));
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@RequestBody Long id) {
        agentRepository.deleteById(id);

        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/deleteAgent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteByAgent(@RequestBody Agent agent) {
        agentRepository.deleteById(agent.getId());
        return new ResponseEntity(new ResponseMessage("Deleting successfully"), HttpStatus.OK);
    }

    @PostMapping("/searchAllColumn")
    public ResponseEntity<?> showEditForm(@RequestBody SearchForm searchString) {
        List<Agent> agents = agentRepository.findAll();
        agents = agents.stream().filter(
                item -> (item.getUser().getEmail().isEmpty() && item.getId().toString().contains(searchString.getSearchString()))
                        || (item.getUser().getEmail().isEmpty() && item.getContent().contains(searchString.getSearchString()))
                        || item.getUser().getFullName().contains(searchString.getSearchString())
                        || (item.getUser().getEmail().isEmpty() && item.getUser().getEmail().contains(searchString.getSearchString()))
        ).collect(Collectors.toList());
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }
}
