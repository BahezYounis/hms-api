package com.aga.hms.infrastructure.store.configuration;

import com.aga.hms.domain.UserStore;
import com.aga.hms.infrastructure.store.DatabaseUserStore;
import com.aga.hms.infrastructure.store.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfiguration {

    @Bean
    UserStore userStore(UserRepository userRepository) { return new DatabaseUserStore(userRepository); }
}
