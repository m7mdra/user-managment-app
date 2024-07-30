package com.example.usermanagmentapp.ui.add;

import com.example.usermanagmentapp.data.model.FormError;

import java.util.List;

/**
 * Represent all possible states of Adding a users
 */
public class AddUserState {
    /**
     * Loading state
     */
    static class Loading extends AddUserState {
    }

    /**
     * Success state
     */
    static class Success extends AddUserState {
    }

    /**
     * Error state
     */
    static class Error extends AddUserState {
    }

    /**
     * Authentication error state
     */
    static class AuthenticationError extends AddUserState {
    }

    /**
     * Validation error state
     */
    static class ValidationError extends AddUserState {
        final List<FormError> list;

        /**
         * Validation error state
         *
         * @param list List of [FormError]
         */
        ValidationError(List<FormError> list) {
            this.list = list;
        }
    }

}

