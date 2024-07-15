package com.aga.hms.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllUsersQuery {

    private final UserStore userStore;

    public Output execute() {
        final var users = userStore.findAll().stream()
                .map(UserStore.UserResult::toDomain)
                .map(ItemOutput::of)
                .toList();
        return new Output(users);
    }

    @Value
    public static class Output {
        List<ItemOutput> users;
    }

    @Value
    public static class ItemOutput {
        UUID id;
        String fullName;
        String email;
        String password;

        static ItemOutput of(User user) {
            return new ItemOutput(user.id(), user.fullName(), user.email(), user.hashedPassword());
        }
    }
}
