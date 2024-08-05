package com.example.usermanagmentapp.data.source;

import com.example.usermanagmentapp.data.AuthorizationError;
import com.example.usermanagmentapp.data.UserNotFoundError;
import com.example.usermanagmentapp.data.UsersFetchError;
import com.example.usermanagmentapp.data.ValidationErrorException;
import com.example.usermanagmentapp.data.model.FormError;
import com.example.usermanagmentapp.data.model.FormErrorsResponse;
import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.network.UsersNetworkService;
import com.google.gson.Gson;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class UserDataSourceTest {

    private final UsersNetworkService networkService = Mockito.mock(UsersNetworkService.class);
    private final UserDataSource dataSource = new UserDataSource(networkService, new Gson());

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testFetchUserListHappyCase() {
        when(networkService.getUsers(anyInt(), anyInt())).thenReturn(
            Single.just(
                Response.success(
                    java.util.Collections.singletonList(User.testUser)
                )
            )
        );
        dataSource.users(1, 10).test()
            .assertNoErrors()
            .assertValue(users -> users.size() == 1);
    }

    @Test
    public void testFetchUserListHappyCaseEmptyUsers() {
        when(networkService.getUsers(anyInt(), anyInt())).thenReturn(
            Single.just(Response.success(null))
        );
        dataSource.users(1, 10).test()
            .assertError(UsersFetchError.class);
    }

    @Test
    public void testFetchUserNotValidHttpCode() {
        when(networkService.getUsers(anyInt(), anyInt())).thenReturn(
            Single.just(
                Response.error(
                    404,
                    ResponseBody.create("", null)
                )
            )
        );
        dataSource.users(1, 1).test()
            .assertError(UnknownError.class);
    }

    @Test
    public void testFetchUserAnyError() {
        when(networkService.getUsers(anyInt(), anyInt())).thenReturn(Single.error(new Exception()));
        dataSource.users(1, 1).test()
            .assertError(Exception.class);
    }

    @Test
    public void testFetchSingleUserHappyCase() {
        when(networkService.getUserDetails(anyInt())).thenReturn(Single.just(Response.success(User.testUser)));
        dataSource.user(1).test()
            .assertNoErrors()
            .assertValue(user -> user.id == -1);
    }

    @Test
    public void testFetchSingleUserNotValidHttpCode() {
        when(networkService.getUserDetails(anyInt())).thenReturn(
            Single.just(
                Response.error(
                    404,
                    ResponseBody.create("", null)
                )
            )
        );
        dataSource.user(1).test()
            .assertError(UserNotFoundError.class);
    }

    @Test
    public void testFetchSingleUserAnyError() {
        when(networkService.getUserDetails(anyInt())).thenReturn(Single.error(new Exception()));
        dataSource.user(1).test()
            .assertError(Exception.class);
    }

    @Test
    public void testAddSingleUserHappyCase() {
        User user = User.testUser;
        when(networkService.addNewUser(any())).thenReturn(Single.just(Response.success(user)));
        dataSource.addUser(user.name, user.gender, user.email, user.status).test()
            .assertComplete()
            .assertNoErrors();
    }

    @Test
    public void testAddSingleUserNotValidHttpCode() {
        User user = User.testUser;
        when(networkService.addNewUser(any())).thenReturn(
            Single.just(
                Response.error(
                    401,
                    ResponseBody.create("", null)
                )
            )
        );
        dataSource.addUser(user.name, user.gender, user.email, user.status).test()
            .assertError(AuthorizationError.class);
    }

    @Test
    public void testAddSingleUserAnyError() {
        User user = User.testUser;
        when(networkService.addNewUser(any())).thenReturn(Single.error(new Exception()));
        dataSource.addUser(user.name, user.gender, user.email, user.status).test()
            .assertError(Exception.class);
    }

    @Test
    public void testAddSingleUserNotValidFormError() {
        User user = User.testUser;
        FormErrorsResponse list = new FormErrorsResponse();
        list.add(new FormError("name", "name is required"));
        String response = new Gson().toJson(list);
        when(networkService.addNewUser(any())).thenReturn(
            Single.just(
                Response.error(
                    422,
                    ResponseBody.create(response, null)
                )
            )
        );
        dataSource.addUser(user.name, user.gender, user.email, user.status)
            .test()
            .assertError(ValidationErrorException.class)
            .assertError(throwable -> {
                ValidationErrorException exception = (ValidationErrorException) throwable;
                return "name".equals(exception.errors.get(0).field);
            });
    }
}
