package com.ltech.uaa.util;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VerifyUserChain {
    private String password;
    private String email;

    public VerifyUserChain password(String password) {
        this.password = password;
        return this;
    }

    public VerifyUserChain email(String email) {
        this.email = email;
        return this;
    }

    public VerifyUserChain verifyPassword() {
        // Verify the password meets the criteria
        if (password == null || !password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$")) {
            throw new IllegalArgumentException("Invalid password");
        }
        return this;
    }

    public VerifyUserChain verifyEmail() {
        // Verify the email format
        if (email == null || !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            throw new IllegalArgumentException("Invalid email");
        }
        return this;
    }

    public boolean isValid() {
        verifyPassword();
        verifyEmail();

        return true;
    }
}
