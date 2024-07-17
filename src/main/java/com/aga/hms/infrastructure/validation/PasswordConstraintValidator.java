package com.aga.hms.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();

        if (password.length() < 8) {
            errorMessages.add("Password shouldn't be less than 8 characters");
        }
        if (!password.matches(".*[!@#$%^&*()_+=|<>?{}\\[\\]~-].*")) {
            errorMessages.add("Password should contain a special character");
        }
        if (!password.matches(".*\\d.*")) {
            errorMessages.add("Password should contain a number");
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String errorMessage : errorMessages) {
                context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
