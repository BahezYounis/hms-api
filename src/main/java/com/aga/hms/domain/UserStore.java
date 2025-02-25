package com.aga.hms.domain;

import com.aga.hms.domain.error.StructuredError;
import com.aga.hms.infrastructure.store.entities.UserEntity;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.Value;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStore {

    Option<UserResult> find(findByIdParams params);

    Option<UserResult> find(findByEmailParams params);

    Either<StructuredError, UserResult> save(saveUserParams params);

    Either<StructuredError, UserResult> update(updateUserParams params);

    List<UserResult> findAll(Specification<UserEntity> spec);


    @Value
    class findByIdParams {
        UUID id;
    }

    @Value
    class findByEmailParams {
        String email;
    }

    @Value
    class findAllUsersParams {
    }

    @Value
    class saveUserParams {
        String fullName;
        String email;
        String password;
    }

    @Value
    class updateUserParams {
        UUID id;
        String fullName;
        String email;
        String password;

        static updateUserParams of(User user) {
            return new updateUserParams(user.id(), user.fullName(), user.email(), user.hashedPassword());
        }
    }

    @Value
    class UserResult {
        UUID id;
        String fullName;
        String email;
        String password;
        Instant createdAt;
        Instant updatedAt;
        Boolean deleted;

        User toDomain() {
            return new User(id, fullName, email, password, createdAt, updatedAt);
        }
    }
}
