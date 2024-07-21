package com.aga.hms.domain;

import com.aga.hms.infrastructure.specification.UserSpecification;
import com.aga.hms.infrastructure.store.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllUsersQuery {

    private final UserStore userStore;


    public Output execute(UserFilterParams filterParams) {

        Specification<UserEntity> spec = Specification.where(null);

        if (filterParams.fullName() != null && !filterParams.fullName().isEmpty()) {
            spec = spec.and(UserSpecification.hasFullName(filterParams.fullName()));
        }

        if (filterParams.email() != null && !filterParams.email().isEmpty()) {
            spec = spec.and(UserSpecification.hasEmail(filterParams.email()));
        }

        if (filterParams.startDate() != null && filterParams.endDate() != null) {
            spec = spec.and(UserSpecification.createdBetween(filterParams.startDate(), filterParams.endDate()));
        }

        final var users = userStore.findAll(spec).stream()
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
        Instant createdAt;

        static ItemOutput of(User user) {
            return new ItemOutput(user.id(), user.fullName(), user.email(), user.hashedPassword(), user.createdAt());
        }
    }
}
