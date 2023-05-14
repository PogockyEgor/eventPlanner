package com.events.eventPlanner.exceptions;

import lombok.Data;

@Data
public class AppError {
    private String errorMessage;
    private int httpStatusCode;

    public AppError(String errorMessage, int httpStatusCode) {
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }
}