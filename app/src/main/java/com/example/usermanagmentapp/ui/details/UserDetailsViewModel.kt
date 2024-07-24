package com.example.usermanagmentapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usermanagmentapp.data.UserNotFoundError
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * responsible for  getting user details logic and state
 * @param repository user repository
 * @param ioScheduler the scheduler to use for background processing
 * @param mainScheduler the scheduler to use for main processing
 */
class UserDetailsViewModel(
    private val repository: UserRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {
    /**
     * state of the view model
     */
    private val _state = MutableLiveData<UserDetailsState>()

    /**
     * live data of the state
     */
    val state: LiveData<UserDetailsState> = _state;

    /**
     * composite disposable to manage subscriptions
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * load user details from the repository
     */
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

    /**
     * clear the composite disposable
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}