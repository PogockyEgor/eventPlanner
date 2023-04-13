package com.events.eventPlanner.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
@Setter
@Getter
public class AppError {
    private String errorMessage;
    private int httpStatusCode;

    public AppError() {
    }

    public AppError(String errorMessage, int httpStatusCode) {
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }
}
