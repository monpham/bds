package backend.realestate.security;

import backend.realestate.model.Role;
import backend.realestate.model.RoleName;
import backend.realestate.model.User;
import backend.realestate.repository.RoleRepository;
import backend.realestate.repository.UserRepository;
import backend.realestate.security.jwt.JwtAuthEntryPoint;
import backend.realestate.security.jwt.JwtAuthTokenFilter;
import backend.realestate.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    ApplicationRunner init(RoleRepository repository, UserRepository userRepository) {
        return args -> {
            if (repository.findAll().size() == 0) {
//                Role roleUser = new Role(RoleName.ROLE_USER);
//                roleUser.setId(1L);
//                Role rolePM = new Role(RoleName.ROLE_PM);
//                roleUser.setId(2L);
//                Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
//                roleUser.setId(3L);
//                repository.save(roleUser);
//                repository.save(rolePM);
//                repository.save(roleAdmin);
//                Set<Role> roles = new HashSet<>();
//                roles.add(repository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("cannot found this role")));
//                User admin = new User("dangdao", "dangdao", "Quocdao.roy@gmail.com",
//                        encoder.encode("dangdao123~!@"), roles
//                );
//                userRepository.save(admin);
            }
        };
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**", "/api/user/**", "/api/socket/**","/images/**").permitAll()
//                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
