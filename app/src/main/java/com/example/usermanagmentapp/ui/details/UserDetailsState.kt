package com.example.usermanagmentapp.ui.details

import com.example.usermanagmentapp.data.model.User

/**
 * A sealed class that represents the state of a [UserDetailsViewModel]
 */
sealed class UserDetailsState {
    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are being fetched
     */
    object Loading : UserDetailsState()

    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are not found
     */
    object UserNotFound : UserDetailsState()

    /**
     * Represents the state of a [UserDetailsViewModel] when an error occurs.
     */
    object Error : UserDetailsState()

    /**
     * Represents the state of a [UserDetailsViewModel] when the user details are fetched successfully
     * @param user
     */
    class Success(val user: User) : UserDetailsState()
}