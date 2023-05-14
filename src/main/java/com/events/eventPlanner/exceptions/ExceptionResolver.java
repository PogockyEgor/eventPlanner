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

import java.util.NoSuchElementException;
import java.util.Set;

@ControllerAdvice
public class ExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<AppError> dataAccessHandler(DataAccessException e) {
        logger.warn("Ошибка при заполнении формы: " + e.getMostSpecificCause());
        return new ResponseEntity<>(new AppError("Неверно заполнена форма, " + e.getMostSpecificCause().getLocalizedMessage(),
                HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> validationHandler(ConstraintViolationException e) {
        logger.warn("Ошибка валидации: " + e.getConstraintViolations());
        StringBuilder errorMessage = new StringBuilder("Ошибка заполнения данных: \n");
        Set<ConstraintViolation<?>> hashSet = e.getConstraintViolations();
        for (ConstraintViolation c : hashSet) {
            errorMessage.append("Ошибка в поле ").append(c.getPropertyPath());
            errorMessage.append(", ").append(c.getMessage()).append(".\n");
        }
        return new ResponseEntity<>(new AppError(errorMessage.toString(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> nullPointerHandler(NoSuchElementException e) {
        logger.warn("Передано значение null: " + e.fillInStackTrace());
        return new ResponseEntity<>(new AppError("Искомый объект не найден",
                HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}