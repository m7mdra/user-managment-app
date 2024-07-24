package com.example.usermanagmentapp.data.network

import com.example.usermanagmentapp.data.model.User
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersNetworkService {
    /**
     * Get a List of Users
     */
    @GET("users")
    fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<Response<List<User>>>

    /**
     * Get a User Details
     */
    @GET("users/{id}")
    fun getUserDetails(@Path("id") id: Int): Single<Response<User>>

    /**
     * Add a new User
     * @param user user to add
     */
    @POST("users")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun addNewUser(@Body user: User): Single<Response<User>>
}