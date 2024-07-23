package com.aga.hms.domain;

import com.aga.hms.domain.error.ErrorType;
import com.aga.hms.domain.error.StructuredError;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class AddUserCommand {

    private final UserStore userStore;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Either<StructuredError, Output> execute(Input input) {
        return validateEmailExistence(
                input.getEmail())
                .flatMap(ignored -> userStore.save(input.toParams(passwordEncoder.encode(input.getPassword()))))
                .map(UserStore.UserResult::toDomain)
                .map(this::generateToken)
                .map(Output::new);
    }

    private Either<StructuredError, Void> validateEmailExistence(String email) {
        return userStore.find(
                new UserStore.findByEmailParams(email))
                .fold(() -> Either.right(null),
                        ignored -> Either.left(new StructuredError("User already created", ErrorType.CONFLICT)));
    }


    private User generateToken(User user) {
        return user;
    }

    @Value
    public static class Input {
        String fullName;
        String email;
        String password;

        private UserStore.saveUserParams toParams(String hashedPassword) {
            return new UserStore.saveUserParams(fullName, email, hashedPassword);
        }
    }

    @Value
    public static class Output {
        User user;
    }
}
