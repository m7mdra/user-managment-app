package com.example.usermanagmentapp.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.di.IOScheduler;
import com.example.usermanagmentapp.di.MainScheduler;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Responsible for getting user from paging data source
 */
@HiltViewModel
public class UserViewModel extends ViewModel {

    private final MutableLiveData<PagingData<User>> _state = new MutableLiveData<>();
    public final LiveData<PagingData<User>> state = _state;
    /**
     * Disposable to clear all subscriptions
     */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final Pager<Integer, User> pager;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    @Inject
    public UserViewModel(Pager<Integer, User> repository,
                         @IOScheduler Scheduler ioScheduler,
                         @MainScheduler Scheduler mainScheduler) {
        this.pager = repository;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    /**
     * Subscribe to paging data source
     */
    public void pagesSubscribe() {

        Disposable disposable = PagingRx.getFlowable(pager)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(_state::setValue, throwable -> {

                });
        compositeDisposable.add(disposable);
    }

    /**
     * Clear all subscriptions
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
