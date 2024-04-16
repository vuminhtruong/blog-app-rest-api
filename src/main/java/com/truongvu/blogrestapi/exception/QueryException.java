package com.truongvu.blogrestapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class QueryException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public QueryException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
