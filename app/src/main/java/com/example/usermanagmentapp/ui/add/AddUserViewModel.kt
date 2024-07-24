package com.example.usermanagmentapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usermanagmentapp.data.AuthorizationError
import com.example.usermanagmentapp.data.ValidationError
import com.example.usermanagmentapp.data.model.UserStatus
import com.example.usermanagmentapp.data.repository.UserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddUserViewModel(
    private val repository: UserRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler,
) : ViewModel() {
    private val _state = MutableLiveData<AddUserState>()
    val state: LiveData<AddUserState> = _state
    private val compositeDisposable = CompositeDisposable()
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
                if(it is AuthorizationError) {
                    _state.postValue(AddUserState.AuthenticationError)
                } else if (it is ValidationError){
                    _state.postValue(AddUserState.ValidationError(it.errors))

                }else{
                    _state.postValue(AddUserState.Error)

                }
            })
        compositeDisposable.add(disposable)
    }
}