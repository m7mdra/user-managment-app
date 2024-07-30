package com.example.usermanagmentapp.data;

import com.example.usermanagmentapp.data.model.FormError;

import java.util.List;

/**
 * A class represent form validation errors.
 */
public class ValidationErrorException extends Exception {
    final List<FormError> errorList;

    public ValidationErrorException(List<FormError> errorList) {
        this.errorList = errorList;
    }
}
