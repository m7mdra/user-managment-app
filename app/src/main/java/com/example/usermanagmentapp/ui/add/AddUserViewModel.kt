package com.example.usermanagmentapp.ui.add

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.ValidationErrorException
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 *
 * responsible for handling the add user logic and state
 * @param repository the repository to use for adding a user
 * @param ioScheduler the scheduler to use for background processing
 * @param mainScheduler the scheduler to use for main processing
 */
class AddUserViewModel(
    private val repository: UserRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler,
) : ViewModel() {

    private val _state = MutableLiveData<AddUserState>()
    val state: LiveData<AddUserState> = _state

    /**
     * Disposables for all subscriptions.
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * Submits a new user to the repository.
     */
    fun submit(name: String, email: String, gender: String, status: UserStatus) {
        val disposable = repository.addUser(name, gender, email, status)
            .doOnSubscribe {
                _state.postValue(AddUserState.Loading)
            }
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe({
                _state.postValue(AddUserState.Success)
            }, {
                when (it) {
                    is AuthorizationError -> _state.postValue(AddUserState.AuthenticationError)
                    is ValidationErrorException -> _state.postValue(AddUserState.ValidationError(it.errors))
                    else -> _state.postValue(AddUserState.Error)
                }
            })
        compositeDisposable.add(disposable)
    }

    /**
     * clear the composite disposable
     */
    @VisibleForTesting
    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}