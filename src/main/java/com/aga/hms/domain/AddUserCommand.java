package com.aga.hms.domain;

import com.aga.hms.domain.error.ErrorType;
import com.aga.hms.domain.error.StructuredError;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AddUserCommand {

    private final UserStore userStore;

    public Either<StructuredError, Output> execute(Input input){
        return validateEmailExistence(input.email)
                .flatMap(ignored -> validatePassword(input.password))
                .flatMap(ignored -> validateFullName(input.fullName))
                .flatMap(ignored -> userStore.save(input.toParams(input.getPassword())))
                .map(UserStore.UserResult::toDomain)
                .map(this::generateToken)
                .map(Output::new);

    }

    private Either<StructuredError, Void> validateEmailExistence(String email){
        return userStore.find(new UserStore.findByEmailParams(email))
                .fold(
                        ()->Either.right(null),
                        ignored -> Either.left(new StructuredError("User already created", ErrorType.CONFLICT))
                );
    }

    private Either<StructuredError, Void> validatePassword(String password) {

        List<String> errorMessages = new ArrayList<>();

        // Check if password length is at least 8 characters
        if (password.length() < 8) {
            errorMessages.add("Password shouldn't be less than 8 characters");
        }

        // Check if password contains a special character
        if (!password.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~-].*")) {
            errorMessages.add("Password should contains special character");
        }

        // Check if password contains a number
        if (!password.matches(".*\\d.*")) {
            errorMessages.add("Password should contains a number");
        }

        if (!errorMessages.isEmpty()) {
            final var errorMessage = String.join(", ", errorMessages);
            return Either.left(new StructuredError(errorMessage, ErrorType.VALIDATION_ERROR));
        }
        return Either.right(null);
    }

    private Either<StructuredError, Void> validateFullName(String fullName) {

        List<String> errorMessages = new ArrayList<>();

        // Check if full name length is at least 4 characters
        if (fullName.length() < 4) {
            errorMessages.add("Profile name shouldn't be less than 4 characters");
        }

        // Check if full name length is at most 8 characters
        if (fullName.length() > 8) {
            errorMessages.add("Profile name shouldn't be more than 8 characters");
        }

        if (!errorMessages.isEmpty()) {
            final var errorMessage = String.join(", ", errorMessages);
            return Either.left(new StructuredError(errorMessage, ErrorType.VALIDATION_ERROR));
        }
        return Either.right(null);
    }


    private String generateToken(User user) {
        return user.email();
    }

    @Value
    public static class Input {
        String fullName;
        String email;
        String password;

        private UserStore.saveUserParams toParams(String hashedPassword) {
            return new UserStore.saveUserParams(
                    fullName,
                    email,
                    hashedPassword
            );
        }
    }

    @Value
    public static class Output {
        String email;
    }
}
