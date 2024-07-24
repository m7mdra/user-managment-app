package com.example.usermanagmentapp.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.usermanagmentapp.RxImmediateSchedulerRule
import com.example.usermanagmentapp.data.UserNotFoundError
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.repository.UserRepository
import com.example.usermanagmentapp.ui.add.AddUserState
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class UserDetailsViewModelTest {
    private val repository = mock<UserRepository>()
    private val testScheduler = Schedulers.trampoline()
    private val testObserver = mock<Observer<UserDetailsState>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var schedulers = RxImmediateSchedulerRule()


    @Test
    fun testGetUserDetailsHappy() {
        val user = User.testUser
        whenever(repository.user(any())).thenReturn(Single.just(user))

        val viewModel = UserDetailsViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.loadData(1)
        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(UserDetailsState.Loading, firstValue)
            @Suppress("USELESS_IS_CHECK")
            assert(secondValue is UserDetailsState.Success)
        }
    }

    @Test
    fun testGetUserDetailsError() {
        whenever(repository.user(any())).thenReturn(Single.error(Throwable()))
        val viewModel = UserDetailsViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.loadData(1)
        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(UserDetailsState.Loading, firstValue)
            @Suppress("USELESS_IS_CHECK")
            assert(secondValue is UserDetailsState.Error)
        }
    }

    @Test
    fun testGetUserUserNotFound() {
        whenever(repository.user(any())).thenReturn(Single.error(UserNotFoundError(1)))
        val viewModel = UserDetailsViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.loadData(1)
        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(UserDetailsState.Loading, firstValue)
            @Suppress("USELESS_IS_CHECK")
            assert(secondValue is UserDetailsState.UserNotFound)
        }
    }
}