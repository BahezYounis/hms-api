package com.aga.hms.domain;

import java.util.UUID;

public record User(
        UUID id,
        String fullName,
        String email,
        String password
) {
}
