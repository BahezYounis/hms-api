package com.aga.hms.domain;

import java.time.Instant;
import java.util.UUID;

public record User(
        UUID id,
        String fullName,
        String email,
        String hashedPassword,
        Instant createdAt
) {
}
