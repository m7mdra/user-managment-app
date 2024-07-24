package com.example.usermanagmentapp.data.source

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.UserNotFound
import com.example.usermanagmentapp.data.UsersFetchError
import com.example.usermanagmentapp.data.ValidationError
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.data.model.FormErrorsResponse
import com.example.usermanagmentapp.data.network.UsersNetworkService
import com.example.usermanagmentapp.data.repository.UserRepository
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class UserDataSource(
    private val networkService: UsersNetworkService,
    private val pager: Pager<Int, User>,
    private val gson: Gson
) :
    UserRepository {
    override fun userPages(): Flowable<PagingData<User>> {
        return pager.flowable
    }

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

    override fun user(id: Int): Single<User> {
        return networkService.getUserDetails(id)
            .flatMap {
                val user = it.body()
                if (it.isSuccessful && user != null) {
                    Single.just(user)
                } else if (it.code() == 404) {
                    Single.error(UserNotFound(id))
                } else {
                    Single.error(UnknownError())
                }
            }
    }

    override fun addUser(
        name: String,
        gender: String,
        email: String,
        status: UserStatus
    ): Completable {
        val newUser = User(
            name = name,
            email = email,
            gender = gender,
            status = status
        )
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