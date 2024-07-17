package com.aga.hms.infrastructure.store.entities;

import com.aga.hms.domain.UserStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(schema = "hms", name = "users")
@SQLDelete(sql = "UPDATE hms SET deleted = true WHERE id=?")
//@Where(clause = "deleted=0")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted")
    private Boolean deleted;


    public static UserEntity of(UserStore.saveUserParams params) {
        return new UserEntity(
                null,
                params.getFullName(),
                params.getEmail(),
                params.getPassword(),
                Instant.now(),
                null,
                Boolean.FALSE
        );
    }

    public UserStore.UserResult toStoreResult() {
        return new UserStore.UserResult(
                id,
                fullName,
                email,
                password
        );
    }

}
