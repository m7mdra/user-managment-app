package com.example.usermanagmentapp.ui.details;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.usermanagmentapp.RxImmediateSchedulerRule;
import com.example.usermanagmentapp.data.UserNotFoundError;
import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.repository.UserRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserDetailsViewModelTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final Scheduler testScheduler = Schedulers.trampoline();
    private final Observer<UserDetailsState> testObserver = mock(Observer.class);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Test
    public void testGetUserDetailsHappy() {
        User user = User.testUser;
        when(repository.user(anyInt())).thenReturn(Single.just(user));

        UserDetailsViewModel viewModel = new UserDetailsViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.load(1);

        ArgumentCaptor<UserDetailsState> captor = ArgumentCaptor.forClass(UserDetailsState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assertEquals(UserDetailsState.Loading.class, captor.getAllValues().get(0).getClass());
        assertEquals(UserDetailsState.Success.class, captor.getAllValues().get(1).getClass());
    }

    @Test
    public void testGetUserDetailsError() {
        when(repository.user(anyInt())).thenReturn(Single.error(new Throwable()));
        UserDetailsViewModel viewModel = new UserDetailsViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.load(1);

        ArgumentCaptor<UserDetailsState> captor = ArgumentCaptor.forClass(UserDetailsState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assertEquals(UserDetailsState.Loading.class, captor.getAllValues().get(0).getClass());
        assertEquals(UserDetailsState.Error.class, captor.getAllValues().get(1).getClass());
    }

    @Test
    public void testGetUserUserNotFound() {
        when(repository.user(anyInt())).thenReturn(Single.error(new UserNotFoundError(1)));
        UserDetailsViewModel viewModel = new UserDetailsViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.load(1);

        ArgumentCaptor<UserDetailsState> captor = ArgumentCaptor.forClass(UserDetailsState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assertEquals(UserDetailsState.Loading.class, captor.getAllValues().get(0).getClass());
        assertEquals(UserDetailsState.UserNotFound.class, captor.getAllValues().get(1).getClass());
    }
}
