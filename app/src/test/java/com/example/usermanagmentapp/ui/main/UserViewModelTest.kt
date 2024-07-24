package com.example.usermanagmentapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.usermanagmentapp.RxImmediateSchedulerRule
import com.example.usermanagmentapp.data.model.User
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserViewModelTest {
    private val pager = mock<Pager<Int,User>>()
    private val testScheduler = Schedulers.trampoline()
    private val testObserver = mock<Observer<PagingData<User>>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var schedulers = RxImmediateSchedulerRule()

    @Test
    fun testGetUsersHappyCase(){
        val expectedPagingData = PagingData.from(listOf(User.testUser))
        whenever(pager.flow).thenReturn(flowOf(expectedPagingData))
        val viewModel = UserViewModel(pager,testScheduler,testScheduler)
        viewModel.pagingData.observeForever(testObserver)
        viewModel.pagesSubscribe()
        argumentCaptor{
            verify(testObserver).onChanged(capture())
            assertEquals(expectedPagingData,firstValue)
        }
    }

}