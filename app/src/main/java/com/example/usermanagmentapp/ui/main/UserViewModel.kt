package com.example.usermanagmentapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.paging.PagingData
import com.example.usermanagmentapp.data.model.User
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable


class UserViewModel(
    private val repository: UserRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler,
) : ViewModel() {
    private val _pagingData = MutableLiveData<PagingData<User>>()
    val pagingData: LiveData<PagingData<User>> = _pagingData;
    private val composeDisposable = CompositeDisposable()

    fun pagesSubscribe() {
        val disposable = repository.userPages()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe({
                _pagingData.value = it
            }, {
                it.printStackTrace()
            })
        composeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        composeDisposable.clear()
    }
}