package com.example.usermanagmentapp.data;

import com.example.usermanagmentapp.data.model.FormError;

import java.util.List;

/**
 * A class represent form validation errors.
 */
public class ValidationErrorException extends Exception {
    public final List<FormError> errors;

    public ValidationErrorException(List<FormError> errorList) {
        this.errors = errorList;
    }
}
