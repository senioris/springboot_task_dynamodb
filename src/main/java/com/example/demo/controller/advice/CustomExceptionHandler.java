package com.example.demo.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.model.BadRequestError;
import com.example.demo.model.NotFoundError;
import com.example.demo.service.task.exception.TaskEntityNotFoundExeption;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TaskEntityNotFoundExeption.class)
    public ResponseEntity<NotFoundError> handleTaskEntityNotFoundException(TaskEntityNotFoundExeption e) {
        var error = new NotFoundError();
        error.setDescription(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestError> handleConstraintViorationException(ConstraintViolationException ex) {
        var error = BadRequestErrorCreator.from(ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }


    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var error = BadRequestErrorCreator.from(ex);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
}
