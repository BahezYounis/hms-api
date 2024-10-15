package com.aga.hms.infrastructure.endpoints;


import com.aga.hms.domain.AddUserCommand;
import com.aga.hms.domain.GetAllUsersQuery;
import com.aga.hms.domain.GetUserQuery;
import com.aga.hms.infrastructure.endpoints.Requests.GetUsersRequest;
import com.aga.hms.infrastructure.endpoints.Response.getUserResponse;
import com.aga.hms.infrastructure.error.ErrorStructureException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    private final GetUserQuery getUserQuery;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    @Operation(summary = "this is for get user detail")
    public getUserResponse getUser(@PathVariable UUID userId) {
        final var output =  getUserQuery.execute(new GetUserQuery.Input(userId));
        return new getUserResponse(
                output.getId(),
                output.getFullName(),
                output.getEmail(),
                output.getPassword(),
                output.getCreatedAt(),
                output.getUpdatedAt()
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddResponse addUser(@Valid @RequestBody GetUsersRequest request) {
        return addUserCommand.execute(new AddUserCommand.Input(request.getFullName(), request.getEmail(), request.getPassword()))
                .map(output -> new AddResponse(output.getFullName(), output.getEmail()))
                .getOrElseThrow(ErrorStructureException::new);
    }

    record AddResponse(String fullName, String email) {
    }

}

