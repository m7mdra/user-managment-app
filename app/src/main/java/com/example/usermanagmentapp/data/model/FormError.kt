package com.example.usermanagmentapp.data.model

/**
 * Represent a form error.
 * @property field The field name.
 * @property message The error message.
 */
data class FormError(
    val `field`: String,
    val message: String
)