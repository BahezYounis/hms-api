package com.aga.hms.infrastructure.error;

import com.aga.hms.domain.error.StructuredError;
import lombok.Getter;

import static com.aga.hms.infrastructure.error.ErrorTypeToHttpStatusMapper.httpStatus;

public class ErrorStructureException extends RuntimeException{

    @Getter
    private final int httpStatus;
    @Getter
    private final String message;

    public ErrorStructureException(StructuredError structuredError) {
        this.httpStatus = httpStatus(structuredError.type());
        this.message = structuredError.message();
    }
}
