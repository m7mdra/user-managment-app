package com.example.usermanagmentapp.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.usermanagmentapp.RxImmediateSchedulerRule
import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.ValidationErrorException
import com.example.usermanagmentapp.data.model.FormError
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
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

class AddUserViewModelTest {
    private val repository = mock<UserRepository>()
    private val testScheduler = Schedulers.trampoline()
    private val testObserver = mock<Observer<AddUserState>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var schedulers = RxImmediateSchedulerRule()

    @Test
    fun testAddUserHappyCase() {
        whenever(repository.addUser(any(), any(), any(), any())).thenReturn(Completable.complete())
        val viewModel =
            AddUserViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.submit("name", "email", "gender", UserStatus.Active)

        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(AddUserState.Loading, firstValue)
            assertEquals(AddUserState.Success, secondValue)
        }
    }

    @Test
    fun testAddUserErrorCase() {
        whenever(repository.addUser(any(), any(), any(), any())).thenReturn(
            Completable.error(
                Exception()
            )
        )
        val viewModel =
            AddUserViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.submit("name", "email", "gender", UserStatus.Active)

        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(AddUserState.Loading, firstValue)
            assertEquals(AddUserState.Error, secondValue)
        }
    }

    @Test
    fun testAddUserAuthenticationError() {
        whenever(repository.addUser(any(), any(), any(), any())).thenReturn(
            Completable.error(
                AuthorizationError()
            )
        )
        val viewModel =
            AddUserViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.submit("name", "email", "gender", UserStatus.Active)
        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(AddUserState.Loading, firstValue)
            assertEquals(AddUserState.AuthenticationError, secondValue)
        }
    }
    @Test
    fun testAddUserGeneralError() {
        whenever(repository.addUser(any(), any(), any(), any())).thenReturn(
            Completable.error(
                UnknownError()
            )
        )
        val viewModel =
            AddUserViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.submit("name", "email", "gender", UserStatus.Active)
        argumentCaptor {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(AddUserState.Loading, firstValue)
            assertEquals(AddUserState.Error, secondValue)
        }
    }

    @Test
    fun testAddUseValidationError() {
        whenever(repository.addUser(any(), any(), any(), any()))
            .thenReturn(
                Completable.error(
                    ValidationErrorException(
                        listOf(FormError("name", "Name is required"))
                    )
                )
            )
        val viewModel =
            AddUserViewModel(repository, testScheduler, testScheduler)
        viewModel.state.observeForever(testObserver)
        viewModel.submit("name", "email", "gender", UserStatus.Active)
        argumentCaptor<AddUserState> {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(AddUserState.Loading, firstValue)
            assert(secondValue is AddUserState.ValidationError)
        }
    }


}

