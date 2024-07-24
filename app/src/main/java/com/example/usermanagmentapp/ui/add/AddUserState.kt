package com.example.usermanagmentapp.ui.add

import com.example.usermanagmentapp.data.model.FormError

/**
 * Represent all possible states of Adding a users
 */
sealed class AddUserState {
    /**
     * Loading state
     */
    object Loading : AddUserState()

    /**
     * Error state
     */
    object Error : AddUserState()

    /**
     * Success state
     */
    object Success : AddUserState()

    /**
     * Authentication error state
     */
    object AuthenticationError : AddUserState()

    /**
     * Validation error state
     * @param errors List of [FormError]
     */
    class ValidationError(val errors: List<FormError>) : AddUserState()

}