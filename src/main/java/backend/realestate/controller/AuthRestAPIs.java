package backend.realestate.controller;

import backend.realestate.message.request.LoginForm;
import backend.realestate.message.request.SignUpForm;
import backend.realestate.message.response.JwtResponse;
import backend.realestate.message.response.ResponseMessage;
import backend.realestate.model.Role;
import backend.realestate.model.RoleName;
import backend.realestate.model.User;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import backend.realestate.security.jwt.JwtProvider;
import backend.realestate.service.UserNotFoundException;
import backend.realestate.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthRestAPIs {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + userDetails.getUsername()));
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), user.getFullName(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> email is already taken"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken"), HttpStatus.BAD_REQUEST);
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), encoder.encode(signUpForm.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find.")));
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ResponseMessage("Resgister successfully!"), HttpStatus.OK);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@RequestBody String email) throws UnsupportedEncodingException, MessagingException {
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://homespace.website:8081/#/" + "reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
        } catch (UserNotFoundException ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseMessage("Resgister successfully!"), HttpStatus.OK);
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        SimpleMailMessage msg= new SimpleMailMessage();
        msg.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        msg.setSubject(subject);
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
       msg.setText(content);
        this.mailSender.send(msg);
    }
    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(@RequestBody Map<String, String> map){
    User user = userRepository.findByResetPasswordToken(map.get("token"));
    user.setPassword(encoder.encode(map.get("password")));
    userRepository.save(user);
        return new ResponseEntity(new ResponseMessage("update password successfully"), HttpStatus.OK);
    }
}
