package com.aga.hms.infrastructure.endpoints;


import com.aga.hms.domain.AddUserCommand;
import com.aga.hms.domain.GetAllUsersQuery;
import com.aga.hms.infrastructure.error.ErrorStructureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public UsersResponse getUsers() {
        final var output = getAllUsersQuery.execute();
        return new UsersResponse(
                output.getUsers()
                        .stream()
                        .map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPassword()))
                        .toList());
    }

    record UsersResponse(List<UserResponse> users) {
    }

    record UserResponse(UUID id, String fullName, String email, String password) {
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AddResponse signup(@RequestBody AddRequest request) {
        return addUserCommand.execute(new AddUserCommand.Input(request.fullName, request.email, request.password))
                .map(output -> new AddResponse(output.getEmail()))
                .getOrElseThrow(ErrorStructureException::new);
    }

    record AddRequest(String fullName, String email, String password) {
    }

    record AddResponse(String token) {
    }

}

