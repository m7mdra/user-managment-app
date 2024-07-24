package com.example.usermanagmentapp.data

import com.example.usermanagmentapp.data.model.FormError

/**
 * A class represent unknown error.
 */
class UnknownError : Error()

/**
 * A class represent that no users found.
 */
class NoUsersFoundError : Error()

/**
 * A class represent users fetch error.
 */
class UsersFetchError : Error()

/**
 * A class represent user not found.
 */
class UserNotFoundError(val userId: Int) : Error()

/**
 * A class represent that caller has no authorization.
 */
class AuthorizationError : Error()

/**
 * A class represent form validation errors.
 */
class ValidationErrorException(val errors: List<FormError>) : Error()