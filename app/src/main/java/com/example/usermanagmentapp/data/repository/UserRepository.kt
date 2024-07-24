package com.example.usermanagmentapp.data.repository

import androidx.paging.PagingData
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.model.UserStatus
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface UserRepository {
    fun users(page: Int, perPage: Int): Single<List<User>>

    fun user(id: Int): Single<User>

    fun addUser(name: String, gender: String, email: String, status: UserStatus): Completable

}