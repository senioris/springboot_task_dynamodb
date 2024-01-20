package com.example.demo.controller.advice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.coyote.BadRequestException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.demo.model.InvalidParam;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;

import com.example.demo.model.BadRequestError;

public class BadRequestErrorCreator {
    public static BadRequestError from(MethodArgumentNotValidException ex) {
        var invalidParamList = createInvalidParamList(ex);

        var error = new BadRequestError();
        error.setInvalidParams(invalidParamList);
        return error;
    }

    public static BadRequestError from(ConstraintViolationException ex) {
        var invalidParamList = createInvalidParamList(ex);

        var err = new BadRequestError();
        err.setInvalidParams(invalidParamList);
        return err;
    }

    private static List<InvalidParam> createInvalidParamList(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
            .stream()
            .map(BadRequestErrorCreator::createInvalidParams)
            .collect(Collectors.toList());
    }

    private static InvalidParam createInvalidParams(ConstraintViolation<?> violation) {
        var parameterOpt = StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
            .filter(node -> {
                return node.getKind().equals(ElementKind.PARAMETER);
            })
            .findFirst();

        var invalidParams = new InvalidParam();
        parameterOpt.ifPresent(p -> {
            invalidParams.setName(p.getName());
        });
        invalidParams.setReason(violation.getMessage());
        return invalidParams;
    }
 
    private static List<InvalidParam> createInvalidParamList(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(BadRequestErrorCreator::createInvalidParam)
        .collect(Collectors.toList());
    }

    private static InvalidParam createInvalidParam(FieldError fieldError) {
        var invalidParam = new InvalidParam();
        invalidParam.setName(fieldError.getField());
        invalidParam.setReason(fieldError.getDefaultMessage());
        return invalidParam;        
    }
}
