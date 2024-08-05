package com.example.usermanagmentapp.data.model;

/**
 * Represent a form error.
 *
 * @property field The field name.
 * @property message The error message.
 */
public class FormError {

    public final String field;
    public final String message;

    public FormError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}

