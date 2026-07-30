package dev.flyfish.validation.example.boot2;

import javax.validation.constraints.NotBlank;

import dev.flyfish.validation.constraints.ChineseMobile;
import dev.flyfish.validation.constraints.StrictEmail;
import dev.flyfish.validation.constraints.StrongPassword;
import dev.flyfish.validation.constraints.Username;
import dev.flyfish.validation.constraints.cross.FieldsMatch;

/** 用户注册命令，演示标准约束、业务约束与跨字段约束组合。 */
@FieldsMatch(first = "password", second = "confirmation",
reportOn = "confirmation")
public class RegisterCommand {
    @NotBlank
    @Username
    private String username;

    @NotBlank
    @ChineseMobile
    private String mobile;

    @NotBlank
    @StrictEmail
    private String email;

    @NotBlank
    @StrongPassword
    private String password;

    @NotBlank
    private String confirmation;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmation() { return confirmation; }
    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
