package com.example.usermanagmentapp.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.usermanagmentapp.data.AuthorizationError;
import com.example.usermanagmentapp.data.ValidationErrorException;
import com.example.usermanagmentapp.data.model.UserStatus;
import com.example.usermanagmentapp.data.repository.UserRepository;
import com.example.usermanagmentapp.di.IOScheduler;
import com.example.usermanagmentapp.di.MainScheduler;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class AddUserViewModel extends ViewModel {
    private final MutableLiveData<AddUserState> _state = new MutableLiveData<>();
    public final LiveData<AddUserState> state = _state;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UserRepository repository;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    @Inject
    public AddUserViewModel(UserRepository repository,
                            @IOScheduler Scheduler ioScheduler,
                            @MainScheduler Scheduler mainScheduler) {
        this.repository = repository;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    public void submit(String name, String email, String gender, UserStatus status) {
        Disposable disposable = repository.addUser(name, gender, email, status)
                .doOnSubscribe(d -> _state.postValue(new AddUserState.Loading()))
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(() -> {
                    _state.postValue(new AddUserState.Success());
                }, throwable -> {
                    if (throwable instanceof AuthorizationError) {
                        _state.postValue(new AddUserState.AuthenticationError());

                    } else if (throwable instanceof ValidationErrorException) {
                        _state.postValue(new AddUserState.ValidationError(((ValidationErrorException) throwable).errors));
                    } else {
                        _state.postValue(new AddUserState.Error());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
