package com.events.eventPlanner.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;
import java.util.Set;

@ControllerAdvice
public class ExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<AppError> dataAccessHandler(DataAccessException e) {
        logger.warn("invalid structure of the transmitted data: " + e.getLocalizedMessage());
        return new ResponseEntity<>(new AppError("Invalid structure of the transmitted data " + e.getMostSpecificCause().getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<AppError> ArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException e) {
        logger.warn("invalid data type transmitted: " + e.getMostSpecificCause());
        return new ResponseEntity<>(new AppError("invalid data type transmitted " + e.getMostSpecificCause().getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenContentException.class)
    public ResponseEntity<AppError> forbiddenContentHandler(ForbiddenContentException e) {
        logger.warn("trying to send request for forbidden object" + e.getLocalizedMessage());
        return new ResponseEntity<>(new AppError("You don't have permission for this request, " +
                e.getLocalizedMessage(), HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<AppError> ObjectNotFoundHandler(ObjectNotFoundException e) {
        logger.warn("the requested object was not found" + e.getLocalizedMessage());
        return new ResponseEntity<>(new AppError("The requested object was not found, " +
                e.getLocalizedMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> validationHandler(ConstraintViolationException e) {
        logger.warn("Validation error: " + e.getConstraintViolations());
        StringBuilder errorMessage = new StringBuilder("Data filling error: \n");
        Set<ConstraintViolation<?>> hashSet = e.getConstraintViolations();
        for (ConstraintViolation c : hashSet) {
            errorMessage.append("Error in row ").append(c.getPropertyPath());
            errorMessage.append(", ").append(c.getMessage()).append(".\n");
        }
        return new ResponseEntity<>(new AppError(errorMessage.toString(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> nullPointerHandler(NoSuchElementException e) {
        logger.warn("returned null value: " + e.fillInStackTrace());
        return new ResponseEntity<>(new AppError("Returned null value",
                HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}