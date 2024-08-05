package com.example.usermanagmentapp.ui.main;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.paging.Pager;
import androidx.paging.PagingData;
import com.example.usermanagmentapp.RxImmediateSchedulerRule;
import com.example.usermanagmentapp.data.model.User;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

public class UserViewModelTest {

    private final Pager<Integer, User> pager = mock(Pager.class);
    private final Scheduler testScheduler = Schedulers.trampoline();
    private final Observer<PagingData<User>> testObserver = mock(Observer.class);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Test
    public void testGetUsersHappyCase() {
        PagingData<User> expectedPagingData = PagingData.from(java.util.Collections.singletonList(User.testUser));
        Flow<PagingData<User>> flow = FlowKt.flowOf(expectedPagingData);

        when(pager.getFlow()).thenReturn(flow);

        UserViewModel viewModel = new UserViewModel(pager, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.pagesSubscribe();

        ArgumentCaptor<PagingData<User>> captor = ArgumentCaptor.forClass(PagingData.class);
        verify(testObserver).onChanged(captor.capture());

        assertEquals(expectedPagingData, captor.getValue());
    }
}
