package com.truongvu.blogrestapi.dto;

import java.util.Date;

public class ErrorDetail {
    private final Date timestamp;
    private final String message;
    private final String detail;

    public ErrorDetail(Date timestamp, String message, String detail) {
        this.timestamp = timestamp;
        this.message = message;
        this.detail = detail;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
