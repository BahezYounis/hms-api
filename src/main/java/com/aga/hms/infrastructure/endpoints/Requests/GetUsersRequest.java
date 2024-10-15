package com.aga.hms.infrastructure.endpoints.Requests;

import com.aga.hms.domain.AddUserCommand;
import com.aga.hms.infrastructure.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class GetUsersRequest {

        @NotBlank(message = "Full name is required")
        @Size(min = 4, max = 8, message = "Full name should be between 4 and 8 characters")
        String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email;

        @NotBlank(message = "Password is required")
        @ValidPassword
        String password;

        AddUserCommand.Input toCommandInput() {
                return new AddUserCommand.Input(
                        this.fullName,
                        this.email,
                        this.password
                );
        }
}
