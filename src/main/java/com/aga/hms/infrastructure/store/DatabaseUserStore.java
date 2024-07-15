package com.aga.hms.infrastructure.store;

import com.aga.hms.domain.UserStore;
import com.aga.hms.domain.error.ErrorType;
import com.aga.hms.domain.error.StructuredError;
import com.aga.hms.infrastructure.TransactionalHms;
import com.aga.hms.infrastructure.store.entities.UserEntity;
import com.aga.hms.infrastructure.store.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DatabaseUserStore implements UserStore {

    private final UserRepository userRepository;

    @Override
    public Option<UserResult> find(findByIdParams params) {
        return Option.ofOptional(userRepository.findById(params.getId()))
                .map(UserEntity::toStoreResult);
    }

    @Override
    public Option<UserResult> find(findByEmailParams params) {
        return userRepository.findByEmail(params.getEmail())
                .map(UserEntity::toStoreResult);
    }

    @Override
    @TransactionalHms
    public Either<StructuredError, UserResult> save(saveUserParams params) {
        return Try.of(() -> UserEntity.of(params))
                .map(userRepository::save)
                .toEither(new StructuredError("Couldn't save user", ErrorType.SERVER_ERROR))
                .map(UserEntity::toStoreResult);
    }

    @Override
    public Either<StructuredError, UserResult> update(updateUserParams params) {
        return null;
    }

    @Override
    public List<UserResult> findAll() {
        return userRepository.findAll().stream().map(UserEntity::toStoreResult).collect(Collectors.toList());
    }


}
