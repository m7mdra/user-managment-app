package com.example.usermanagmentapp.ui.details

import com.example.usermanagmentapp.data.model.User

sealed class UserDetailsState {
    object Loading : UserDetailsState()
    object UserNotFound : UserDetailsState()
    object Error : UserDetailsState()
    class Success(val user: User) : UserDetailsState()
}