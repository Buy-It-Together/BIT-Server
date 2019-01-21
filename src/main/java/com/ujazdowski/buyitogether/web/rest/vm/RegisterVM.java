package com.ujazdowski.buyitogether.web.rest.vm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterVM implements Serializable {
    @Email
    @Size(min = 5, max = 254)
    private String email;
    @NotBlank
    @Size(min = 8, max = 254)
    private String password;

    public RegisterVM(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public RegisterVM() {
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
}
