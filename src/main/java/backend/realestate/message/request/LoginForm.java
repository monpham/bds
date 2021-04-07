package backend.realestate.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginForm {
    @NotBlank
    @Size(min = 3, max = 50, message = "Tên phải bé hơn 3 và lớn hơn 50")
    private String username;

    @NotBlank
    @Size(min = 6, max = 50, message = "Tên phải bé hơn 6 và lớn hơn 50")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
