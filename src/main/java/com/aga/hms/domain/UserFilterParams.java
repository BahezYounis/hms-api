package com.aga.hms.domain;

import java.time.LocalDateTime;

public record UserFilterParams(
        String fullName,
        String email,
        LocalDateTime startDate,
        LocalDateTime endDate) {
}
