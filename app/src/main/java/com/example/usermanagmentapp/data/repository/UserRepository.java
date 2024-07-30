package com.example.usermanagmentapp.data.repository;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.model.UserStatus;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    /**
     * Get a list of users from the a source.
     *
     * @param page    Page number
     * @param perPage Number of users per page
     * @return [List] of [User] or an error
     */
    Single<List<User>> users(int page, int perPage);

    /**
     * Get a user from the a source.
     *
     * @param id User id
     * @return [User]
     */
    Single<User> user(int id);

    /**
     * Add a new user to the a source.
     *
     * @param name   name
     * @param gender gender
     * @param email  email
     * @param status status
     * @return [Completable] that return no value
     */
    Completable addUser(String name, String gender, String email, UserStatus status);

}
