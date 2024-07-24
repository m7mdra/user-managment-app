package com.example.usermanagmentapp.data.source

import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.UserNotFoundError
import com.example.usermanagmentapp.data.UsersFetchError
import com.example.usermanagmentapp.data.ValidationError
import com.example.usermanagmentapp.data.model.FormError
import com.example.usermanagmentapp.data.model.FormErrorsResponse
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.network.UsersNetworkService
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


class UserDataSourceTest {
    private val networkService = mock<UsersNetworkService>()
    private val dataSource = UserDataSource(networkService, Gson())

    @Test
    fun testFetchUserListHappyCase() {
        whenever(networkService.getUsers(any(), any())).thenReturn(
            Single.just(
                Response.success(
                    listOf(User.testUser())
                )
            )
        )
        dataSource.users(1, 10).test()
            .assertNoErrors()
            .assertValue { it.size == 1 }
    }

    @Test
    fun testFetchUserListHappyCaseEmptyUsers() {
        whenever(
            networkService.getUsers(
                any(),
                any()
            )
        ).thenReturn(Single.just(Response.success(null)))
        dataSource.users(1, 10).test()
            .assertError(UsersFetchError::class.java)
    }

    @Test
    fun testFetchUserNotValidHttpCode() {
        whenever(networkService.getUsers(any(), any())).thenReturn(
            Single.just(
                Response.error(
                    404,
                    "".toResponseBody()
                )
            )
        )
        dataSource.users(1, 1).test()
            .assertError(UnknownError::class.java)
    }

    @Test
    fun testFetchUserAnyError() {
        whenever(networkService.getUsers(any(), any())).thenReturn(Single.error(Exception()))
        dataSource.users(1, 1).test()
            .assertError(Exception::class.java)
    }

    @Test
    fun testFetchSingleUserHappyCase() {
        whenever(networkService.getUserDetails(any())).thenReturn(Single.just(Response.success(User.testUser())))
        dataSource.user(1).test()
            .assertNoErrors()
            .assertValue { it.id == -1 }
    }

    @Test
    fun testFetchSingleUserNotValidHttpCode() {
        whenever(networkService.getUserDetails(any())).thenReturn(
            Single.just(
                Response.error(
                    404,
                    "".toResponseBody()
                )
            )
        )
        dataSource.user(1).test()
            .assertError(UserNotFoundError::class.java)

    }

    @Test
    fun testFetchSingleUserAnyError() {
        whenever(networkService.getUserDetails(any())).thenReturn(Single.error(Exception()))
        dataSource.user(1).test()
            .assertError(Exception::class.java)
    }

    @Test
    fun testAddSingleUserHappyCase() {
        val user = User.testUser()
        whenever(networkService.addNewUser(any())).thenReturn(Single.just(Response.success(user)))
        dataSource.addUser(user.name!!, user.gender!!, user.email!!, user.status!!).test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun testAddSingleUserNotValidHttpCode() {
        val user = User.testUser()
        whenever(networkService.addNewUser(any())).thenReturn(
            Single.just(
                Response.error(
                    401,
                    "".toResponseBody()
                )
            )
        )
        dataSource.addUser(user.name!!, user.gender!!, user.email!!, user.status!!)
            .test()
            .assertError(AuthorizationError::class.java)
    }

    @Test
    fun testAddSingleUserAnyError() {
        val user = User.testUser()
        whenever(networkService.addNewUser(any())).thenReturn(Single.error(Exception()))
        dataSource.addUser(user.name!!, user.gender!!, user.email!!, user.status!!)
            .test()
            .assertError(Exception::class.java)
    }

    @Test
    fun testAddSingleUserNotValidFormError() {
        val user = User.testUser()
        val list = FormErrorsResponse()
        list.add(FormError("name", "name is required"))
        val response = Gson().toJson(list)
        whenever(networkService.addNewUser(any())).thenReturn(
            Single.just(
                Response.error(
                    422,
                    response.toResponseBody()
                )
            )
        )
        dataSource.addUser(user.name!!, user.gender!!, user.email!!, user.status!!)
            .test()
            .assertError(ValidationError::class.java)
            .assertError {
                val exception = it as ValidationError
                exception.errors.first().field == "name"
            }
    }
}