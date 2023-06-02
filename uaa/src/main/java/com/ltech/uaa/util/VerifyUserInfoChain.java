package com.ltech.uaa.util;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VerifyUserInfoChain {
    private final Set<Boolean> isValidSet;

    VerifyUserInfoChain() {
        this.isValidSet = new HashSet<>();
    }
    public VerifyUserInfoChain checkPassword(String password) {
         isValidSet.add( password.length() >= 4);

        return this;
    }

    public VerifyUserInfoChain checkEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        isValidSet.add(matcher.matches());

        return this;
    }

    public boolean isValid() {
        return isValidSet.size() == 1 && isValidSet.contains(true);
    }
}
