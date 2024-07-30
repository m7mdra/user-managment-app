package com.example.usermanagmentapp.ui.details;

import com.example.usermanagmentapp.data.model.User;

/**
 * A sealed class that represents the state of a [UserDetailsViewModel]
 */
public class UserDetailsState {
    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are being fetched
     */
    static class Loading extends UserDetailsState {
    }

    /**
     * Represents the state of a [UserDetailsViewModel] when an error occurs.
     */
    static class Error extends UserDetailsState {
    }

    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are fetched successfully
     *
     */
    static class Success extends UserDetailsState {
        final User user;

        /**
         * Represents the state of a [UserDetailsViewModel] when the user details are fetched successfully
         *
         * @param user user
         */
        Success(User user) {
            this.user = user;
        }
    }

    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are not found
     */
    static class UserNotFound extends UserDetailsState {

    }
}
