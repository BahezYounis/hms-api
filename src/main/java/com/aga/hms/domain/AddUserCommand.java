package com.aga.hms.domain;

import com.aga.hms.domain.error.ErrorType;
import com.aga.hms.domain.error.StructuredError;
import com.aga.hms.infrastructure.validation.ValidPassword;
import io.vavr.control.Either;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class AddUserCommand {

    private final UserStore userStore;

    public Either<StructuredError, Output> execute(Input input) {
        return validateEmailExistence(
                input.getEmail())
                .flatMap(ignored -> userStore.save(input.toParams(input.getPassword())))
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


    private String generateToken(User user) {
        return user.email();
    }

    @Value
    public static class Input {
        @NotBlank(message = "Full name is required")
        @Size(min = 4, max = 8, message = "Full name should be between 4 and 8 characters")
        String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email;

        @NotBlank(message = "Password is required")
        @ValidPassword
        String password;

        private UserStore.saveUserParams toParams(String hashedPassword) {
            return new UserStore.saveUserParams(fullName, email, hashedPassword);
        }
    }

    @Value
    public static class Output {
        String email;
    }
}
