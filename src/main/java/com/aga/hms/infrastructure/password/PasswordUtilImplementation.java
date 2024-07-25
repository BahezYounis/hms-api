package com.aga.hms.infrastructure.password;

import com.aga.hms.domain.PasswordUtil;
import lombok.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Value
public class PasswordUtilImplementation implements PasswordUtil {

    PasswordEncoder passwordEncoder;

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean validate(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
