package com.example.usermanagmentapp.data.source

import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.UserNotFoundError
import com.example.usermanagmentapp.data.UsersFetchError
import com.example.usermanagmentapp.data.ValidationError
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.data.model.FormErrorsResponse
import com.example.usermanagmentapp.data.network.UsersNetworkService
import com.example.usermanagmentapp.data.repository.UserRepository
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserDataSource(
    private val networkService: UsersNetworkService,
    private val gson: Gson
) : UserRepository {

    /**
     * Get a list of users from the a network source.
     * @param page Page number
     * @param perPage Number of users per page
     * @return [List] of [User] or an error, could be [UsersFetchError] or [UnknownError]
     */
    override fun users(page: Int, perPage: Int): Single<List<User>> {
        return networkService.getUsers(page, perPage)
            .flatMap {
                if (it.isSuccessful) {
                    val userList = it.body()
                    if (userList.isNullOrEmpty()) {
                        Single.error(UsersFetchError())
                    } else {
                        Single.just(userList)
                    }
                } else {
                    Single.error(UnknownError())
                }
            }
    }
    /**
     * Get a user from the a source.
     * @param id User id
     * @return [User] or an error, could be [UserNotFoundError] or [UnknownError]
     */
    override fun user(id: Int): Single<User> {
        return networkService.getUserDetails(id)
            .flatMap {
                val user = it.body()
                if (it.isSuccessful && user != null) {
                    Single.just(user)
                } else if (it.code() == 404) {
                    Single.error(UserNotFoundError(id))
                } else {
                    Single.error(UnknownError())
                }
            }
    }
    /**
     * Add a new user to the a source.
     * @param name
     * @param gender
     * @param email
     * @param status
     * @return [Completable] that return no value or an error,
     * could be [AuthorizationError] or [ValidationError]
     */
    override fun addUser(
        name: String,
        gender: String,
        email: String,
        status: UserStatus
    ): Completable {
        val newUser = User(name = name, email = email, gender = gender, status = status)
        return networkService.addNewUser(newUser)
            .flatMapCompletable {
                val user = it.body()
                if (it.isSuccessful && user != null) {
                    Completable.complete()
                } else if (it.code() == 401) {
                    Completable.error(AuthorizationError())
                } else if (it.code() == 422) {
                    val errors = gson.fromJson(
                        it.errorBody()?.string(),
                        FormErrorsResponse::class.java
                    )
                    Completable.error(ValidationError(errors))
                } else {
                    Completable.error(UnknownError())
                }
            }
    }
}