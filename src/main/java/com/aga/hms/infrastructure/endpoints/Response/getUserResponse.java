package com.aga.hms.infrastructure.endpoints.Response;

import java.time.Instant;
import java.util.UUID;

public record getUserResponse(
        UUID Id,
        String fullName,
        String email,
        String password,
        Instant createdAt,
        Instant updatedAt
) {
}
