package com.aga.hms.infrastructure.configuration;

import com.aga.hms.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    GetAllUsersQuery getAllUsersQuery(UserStore userStore) {
        return new GetAllUsersQuery(userStore);
    }

    @Bean
    AddUserCommand addUserCommand(UserStore userStore, PasswordUtil passwordUtil) {
        return new AddUserCommand(userStore, passwordUtil);
    }

    @Bean
    GetUserQuery getUserQuery(UserStore userStore) {
        return new GetUserQuery(userStore);
    }

}
