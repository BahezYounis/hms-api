package com.aga.hms.infrastructure.endpoints;


import com.aga.hms.domain.AddUserCommand;
import com.aga.hms.domain.GetAllUsersQuery;
import com.aga.hms.domain.User;
import com.aga.hms.infrastructure.error.ErrorStructureException;
import com.aga.hms.infrastructure.validation.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final GetAllUsersQuery getAllUsersQuery;
    private final AddUserCommand addUserCommand;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        final var output = getAllUsersQuery.execute(new GetAllUsersQuery.Input(fullName, email, startDate, endDate));
        return new UsersResponse(
                output.getUsers()
                        .stream()
                        .map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getCreatedAt()))
                        .toList());
    }

    record UsersResponse(List<UserResponse> users) {
    }

    record UserResponse(UUID id, String fullName, String email, Instant created) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public AddResponse addUser(@Valid @RequestBody AddRequest request) {
        return addUserCommand.execute(new AddUserCommand.Input(request.fullName(), request.email(), request.password()))
                .map(output -> new AddResponse(output.getUser()))
                .getOrElseThrow(ErrorStructureException::new);
    }

    record AddRequest(
            @NotBlank(message = "Full name is required")
            @Size(min = 4, max = 8, message = "Full name should be between 4 and 8 characters")
            String fullName,
            @NotBlank(message = "Email is required")
            @Email(message = "Email should be valid")
            String email,
            @NotBlank(message = "Password is required")
            @ValidPassword
            String password) {
    }

    record AddResponse(User user) {
    }

}

