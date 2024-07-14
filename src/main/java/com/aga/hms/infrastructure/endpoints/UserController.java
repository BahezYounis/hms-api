package com.aga.hms.infrastructure.endpoints;


import com.aga.hms.domain.GetAllUsersQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final GetAllUsersQuery getAllUsersQuery;

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
}

