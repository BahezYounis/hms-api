package com.aga.hms.infrastructure.store.repositories;

import com.aga.hms.infrastructure.store.entities.UserEntity;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    Option<UserEntity> findByEmail(String email);

}
