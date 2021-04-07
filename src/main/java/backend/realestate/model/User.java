package backend.realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50")
    @NotBlank(message = "Thông tin không được bỏ trống")
    private String fullName;
    @Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50")
    @NotBlank(message = "Thông tin không được bỏ trống")
    @Column(name = "username", length = 30)
    private String username;
    @NotBlank
    @NaturalId
    @Size(max = 50)
    @Email(message = "Sai định dạng email")
    @Column(name = "email", length = 30)
    private String email;
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Agent agent;
    @OneToMany(mappedBy = "toUser",
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Message> message;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private String image;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public User(@Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50") @NotBlank(message = "Thông tin không được bỏ trống") String fullName
            , @Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50") @NotBlank(message = "Thông tin không được bỏ trống") String username
            , @NotBlank @Size(max = 50) @Email(message = "Sai định dạng email") String email
            , @NotBlank @Size(min = 6, max = 100) String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(@Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50") @NotBlank(message = "Thông tin không được bỏ trống") String fullName, @Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50") @NotBlank(message = "Thông tin không được bỏ trống") String username, @NotBlank @Size(max = 50) @Email(message = "Sai định dạng email") String email, @NotBlank @Size(min = 6, max = 100) String password, Set<Role> roles) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
