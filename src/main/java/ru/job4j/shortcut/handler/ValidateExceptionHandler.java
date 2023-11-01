package ru.job4j.shortcut.handler;

import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidateExceptionHandler {

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s",
                                        f.getDefaultMessage(),
                                        f.getRejectedValue())
                        )).toList()
        );
    }

    @ExceptionHandler (ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(
                e.getConstraintViolations().stream()
                        .map(c -> Map.of(
                                ((PathImpl) c.getPropertyPath()).getLeafNode().getName(),
                                String.format("%s. Actual value: %s",
                                        c.getMessage(),
                                        c.getInvalidValue()))).toList()
        );
    }

}