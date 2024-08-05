package com.example.usermanagmentapp.ui.add;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import com.example.usermanagmentapp.RxImmediateSchedulerRule;
import com.example.usermanagmentapp.data.AuthorizationError;
import com.example.usermanagmentapp.data.ValidationErrorException;
import com.example.usermanagmentapp.data.model.FormError;
import com.example.usermanagmentapp.data.model.UserStatus;
import com.example.usermanagmentapp.data.repository.UserRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddUserViewModelTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final Scheduler testScheduler = Schedulers.trampoline();
    private final Observer<AddUserState> testObserver = mock(Observer.class);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Test
    public void testAddUserHappyCase() {
        when(repository.addUser(anyString(), anyString(), anyString(), any())).thenReturn(Completable.complete());
        AddUserViewModel viewModel = new AddUserViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.submit("name", "email", "gender", UserStatus.Active);

        ArgumentCaptor<AddUserState> captor = ArgumentCaptor.forClass(AddUserState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assert(captor.getAllValues().get(0) instanceof  AddUserState.Loading);
        assert(captor.getAllValues().get(1) instanceof  AddUserState.Success);

    }

    @Test
    public void testAddUserErrorCase() {
        when(repository.addUser(anyString(), anyString(), anyString(), any())).thenReturn(Completable.error(new Exception()));
        AddUserViewModel viewModel = new AddUserViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.submit("name", "email", "gender", UserStatus.Active);

        ArgumentCaptor<AddUserState> captor = ArgumentCaptor.forClass(AddUserState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assert(captor.getAllValues().get(0) instanceof  AddUserState.Loading);
        assert(captor.getAllValues().get(1) instanceof  AddUserState.Error);
    }

    @Test
    public void testAddUserAuthenticationError() {
        when(repository.addUser(anyString(), anyString(), anyString(), any())).thenReturn(Completable.error(new AuthorizationError()));
        AddUserViewModel viewModel = new AddUserViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.submit("name", "email", "gender", UserStatus.Active);

        ArgumentCaptor<AddUserState> captor = ArgumentCaptor.forClass(AddUserState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assert(captor.getAllValues().get(0) instanceof  AddUserState.Loading);
        assert(captor.getAllValues().get(1) instanceof  AddUserState.AuthenticationError);
    }

    @Test
    public void testAddUserGeneralError() {
        when(repository.addUser(anyString(), anyString(), anyString(), any())).thenReturn(Completable.error(new UnknownError()));
        AddUserViewModel viewModel = new AddUserViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.submit("name", "email", "gender", UserStatus.Active);

        ArgumentCaptor<AddUserState> captor = ArgumentCaptor.forClass(AddUserState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assert(captor.getAllValues().get(0) instanceof  AddUserState.Loading);
        assert(captor.getAllValues().get(1) instanceof  AddUserState.Error);
    }

    @Test
    public void testAddUserValidationError() {
        when(repository.addUser(anyString(), anyString(), anyString(), any()))
            .thenReturn(Completable.error(new ValidationErrorException(
                java.util.Collections.singletonList(new FormError("name", "Name is required"))
            )));
        AddUserViewModel viewModel = new AddUserViewModel(repository, testScheduler, testScheduler);
        viewModel.state.observeForever(testObserver);
        viewModel.submit("name", "email", "gender", UserStatus.Active);

        ArgumentCaptor<AddUserState> captor = ArgumentCaptor.forClass(AddUserState.class);
        verify(testObserver, times(2)).onChanged(captor.capture());
        assert(captor.getAllValues().get(0) instanceof  AddUserState.Loading);
        assert(captor.getAllValues().get(1) instanceof  AddUserState.ValidationError);
    }
}
