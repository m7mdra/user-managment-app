package com.example.usermanagmentapp.data.source;

import com.example.usermanagmentapp.data.AuthorizationError;
import com.example.usermanagmentapp.data.UserNotFoundError;
import com.example.usermanagmentapp.data.UsersFetchError;
import com.example.usermanagmentapp.data.ValidationErrorException;
import com.example.usermanagmentapp.data.model.FormError;
import com.example.usermanagmentapp.data.model.FormErrorsResponse;
import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.model.UserStatus;
import com.example.usermanagmentapp.data.network.UsersNetworkService;
import com.example.usermanagmentapp.data.repository.UserRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;

public class UserDataSource implements UserRepository {
    private final UsersNetworkService networkService;
    private final Gson gson;

    public UserDataSource(UsersNetworkService networkService, Gson gson) {
        this.networkService = networkService;
        this.gson = gson;
    }

    /**
     * Get a list of users from the a network source.
     *
     * @param page    Page number
     * @param perPage Number of users per page
     * @return [List] of [User] or an error, could be [UsersFetchError] or [UnknownError]
     */
    @Override
    public Single<List<User>> users(int page, int perPage) {
        return networkService.getUsers(page, perPage)
                .flatMap(listResponse -> {
                    if (listResponse.isSuccessful()) {
                        List<User> body = listResponse.body();
                        if (body == null) {
                            return Single.error(new UsersFetchError());
                        } else {
                            return Single.just(body);
                        }
                    } else {
                        return Single.error(new UnknownError());
                    }
                });

    }

    /**
     * Get a user from the a source.
     *
     * @param id User id
     * @return [User] or an error, could be [UserNotFoundError] or [UnknownError]
     */
    @Override
    public Single<User> user(int id) {
        return networkService.getUserDetails(id)
                .flatMap(userResponse -> {

                    if (userResponse.isSuccessful() && userResponse.body() != null) {
                        User user = userResponse.body();
                        return Single.just(user);
                    } else if (userResponse.code() == 404) {
                        return Single.error(new UserNotFoundError(id));
                    } else {
                        return Single.error(new UnknownError());

                    }
                });
    }

    /**
     * Add a new user to the a source.
     * @param name name
     * @param gender gender
     * @param email email
     * @param status status
     * @return [Completable] that return no value or an error,
     * could be [AuthorizationError] or [ValidationErrorException]
     */
    @Override
    public Completable addUser(String name, String gender, String email, UserStatus status) {
        User newUser = new User(-1, name, email, gender, status);
        return networkService.addNewUser(newUser)
                .flatMapCompletable(response -> {
                    User user = response.body();
                    if (response.isSuccessful() && user != null) {
                        return Completable.complete();
                    } else if (response.code() == 401) {
                        return Completable.error(new AuthorizationError());
                    } else if (response.code() == 422) {
                        List<FormError> list = new ArrayList<>();
                        try (ResponseBody responseBody = response.errorBody()) {

                            if (responseBody != null) {
                                String string = responseBody.string();
                                List<FormError> errors = gson.fromJson(string, FormErrorsResponse.class);
                                list.addAll(errors);
                            }
                        }
                        return Completable.error(new ValidationErrorException(list));
                    } else {
                        return Completable.error(new UnknownError());
                    }
                });
    }
}
