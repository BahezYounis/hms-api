package com.aga.hms.domain;

import com.aga.hms.domain.error.ErrorType;
import com.aga.hms.domain.error.StructuredError;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class AddUserCommand {

    private final UserStore userStore;
    private final PasswordUtil passwordUtil;

    public Either<StructuredError, Output> execute(Input input) {
        return validateEmailExistence(
                input.getEmail())
                .flatMap(ignored -> userStore.save(input.toParams(passwordUtil.hash(input.getPassword()))))
                .map(UserStore.UserResult::toDomain)
                .map(user -> new Output(user.fullName(), user.email()));
    }

    private Either<StructuredError, Void> validateEmailExistence(String email) {
        return userStore.find(
                new UserStore.findByEmailParams(email))
                .fold(() -> Either.right(null),
                        ignored -> Either.left(new StructuredError("User already created", ErrorType.CONFLICT)));
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
        String fullName;
        String email;
    }
}
