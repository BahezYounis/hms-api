package com.aga.hms.infrastructure.specification;

import com.aga.hms.infrastructure.store.entities.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    public static Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<UserEntity> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            Predicate startPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            Predicate endPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            return criteriaBuilder.and(startPredicate, endPredicate);
        };
    }
}
