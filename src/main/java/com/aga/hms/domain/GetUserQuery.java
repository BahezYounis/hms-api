package com.aga.hms.domain;

import com.aga.hms.domain.UserStore.findByIdParams;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUserQuery {

    private final UserStore userStore;

    public Output execute(Input input) {
        findByIdParams params = new findByIdParams(input.getId());

        Option<UserStore.UserResult> userOption = userStore.find(params);

        return userOption.map(Output::fromUserResult)
                .getOrElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Value
    public static class Input {
        UUID Id;
    }

    @Value
    public static class Output {
        UUID Id;
        String fullName;
        String email;
        String password;
        Instant createdAt;
        Instant updatedAt;

        public static Output fromUserResult(UserStore.UserResult userResult) {
           return new Output(
                   userResult.getId(),
                   userResult.getFullName(),
                   userResult.getEmail(),
                   userResult.getPassword(),
                   userResult.getCreatedAt(),
                   userResult.getUpdatedAt()
           );
        }
    }
}
