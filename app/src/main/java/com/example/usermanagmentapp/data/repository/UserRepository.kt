package com.example.usermanagmentapp.data.repository

import com.example.usermanagmentapp.data.UserNotFoundError

import com.example.usermanagmentapp.data.UsersFetchError
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.model.UserStatus
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlin.jvm.Throws

/**
 * Entry point for accessing users data.
 */
interface UserRepository {
    /**
     * Get a list of users from the a source.
     * @param page Page number
     * @param perPage Number of users per page
     * @return [List] of [User] or an error
     */
    fun users(page: Int, perPage: Int): Single<List<User>>

    /**
     * Get a user from the a source.
     * @param id User id
     * @return [User]
     */
    fun user(id: Int): Single<User>

    /**
     * Add a new user to the a source.
     * @param name
     * @param gender
     * @param email
     * @param status
     * @return [Completable] that return no value
     */
    fun addUser(name: String, gender: String, email: String, status: UserStatus): Completable

}