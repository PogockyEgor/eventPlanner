package com.events.eventPlanner.exceptions;

public class TwicedUniqueValuesException extends Exception{
    public TwicedUniqueValuesException() {
    }

    public TwicedUniqueValuesException(String message) {
        super(message);
    }

    public TwicedUniqueValuesException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwicedUniqueValuesException(Throwable cause) {
        super(cause);
    }

    public TwicedUniqueValuesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
