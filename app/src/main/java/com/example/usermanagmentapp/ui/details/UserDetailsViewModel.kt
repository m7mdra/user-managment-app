package com.example.usermanagmentapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usermanagmentapp.data.UserNotFoundError
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

class UserDetailsViewModel(
    private val repository: UserRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {
    private val _state = MutableLiveData<UserDetailsState>()
    val state: LiveData<UserDetailsState> = _state;
    private val compositeDisposable = CompositeDisposable()
    fun loadData(id: Int) {
        val disposable = repository.user(id)
            .doOnSubscribe {
                _state.postValue(UserDetailsState.Loading)
            }
            .observeOn(mainScheduler)
            .subscribeOn(ioScheduler)
            .subscribe({
                _state.postValue(UserDetailsState.Success(it))
            }, {
                if (it is UserNotFoundError) {
                    _state.postValue(UserDetailsState.UserNotFound)
                } else {
                    _state.postValue(UserDetailsState.Error)
                }
            })
        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}