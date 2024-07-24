package com.example.usermanagmentapp.ui.add

import com.example.usermanagmentapp.data.model.FormError

sealed class AddUserState {

    object Loading : AddUserState()
    object Error : AddUserState()
    object Success : AddUserState()
    object AuthenticationError : AddUserState()
    class ValidationError(val errors: List<FormError>) : AddUserState()

}