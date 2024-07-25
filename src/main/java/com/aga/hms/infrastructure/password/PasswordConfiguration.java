package com.aga.hms.infrastructure.password;

import com.aga.hms.domain.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordUtil generatedPassword(PasswordEncoder passwordEncoder) {
        return new PasswordUtilImplementation(passwordEncoder);
    }

}
