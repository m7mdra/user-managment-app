package com.example.usermanagmentapp.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.usermanagmentapp.data.UserNotFoundError;
import com.example.usermanagmentapp.data.repository.UserRepository;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * responsible for  getting user details logic and state
 */
public class UserDetailsViewModel extends ViewModel {
    private final MutableLiveData<UserDetailsState> _state = new MutableLiveData<>();
    public final LiveData<UserDetailsState> state = _state;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UserRepository repository;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    /**
     * responsible for  getting user details logic and state
     *
     * @param repository    user repository
     * @param ioScheduler   the scheduler to use for background processing
     * @param mainScheduler the scheduler to use for main processing
     */
    public UserDetailsViewModel(UserRepository repository, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.repository = repository;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    public void load(int id) {
        Disposable disposable = repository.user(id)
                .doOnSubscribe(d -> {
                    _state.postValue(new UserDetailsState.Loading());
                })
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(user -> {
                    _state.postValue(new UserDetailsState.Success(user));

                }, throwable -> {
                    if (throwable instanceof UserNotFoundError) {
                        _state.postValue(new UserDetailsState.UserNotFound());
                    } else {
                        _state.postValue(new UserDetailsState.Error());
                    }
                });
        compositeDisposable.add(disposable);
    }
}
