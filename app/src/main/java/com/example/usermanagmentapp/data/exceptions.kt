package com.example.usermanagmentapp.data

import com.example.usermanagmentapp.data.model.FormError

class NoUsersFoundError : Error()
class UsersFetchError : Error()
class UserNotFound(val userId: Int) : Error()
class AuthorizationError : Error()


class ValidationError(val errors: List<FormError>) : Error()