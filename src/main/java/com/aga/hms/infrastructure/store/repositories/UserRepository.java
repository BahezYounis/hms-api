package com.aga.hms.infrastructure.store.repositories;

import com.aga.hms.infrastructure.store.entities.UserEntity;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Option<UserEntity> findByEmail(String email);

}
