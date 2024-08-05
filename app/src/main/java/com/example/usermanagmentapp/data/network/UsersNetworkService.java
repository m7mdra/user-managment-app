package com.example.usermanagmentapp.data.network;

import com.example.usermanagmentapp.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersNetworkService {

    /**
     * Get a List of Users
     */
    @GET("users")
    Single<Response<List<User>>> getUsers(@Query("page") int page, @Query("per_page") int perPage);

    /**
     * Get a User Details
     */
    @GET("users/{id}")
    Single<Response<User>> getUserDetails(@Path("id") int id);

    /**
     * Add a new User
     *
     * @param user user to add
     */
    @POST("users")
    Single<Response<User>> addNewUser(@Body User user);

}
