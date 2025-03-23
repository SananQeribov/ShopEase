package com.example.shopease.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usercase.LoginUserCase
import com.example.domain.util.ResultWrapper
import com.example.shopease.ShopperSession
import com.example.shopease.utils.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUserCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val response = loginUseCase.execute(email, password)
            when (response) {
                is ResultWrapper.Success -> {
                    ShopperSession.storeUser(response.value)
                    _loginState.value = LoginState.Success()
                }

                is ResultWrapper.Failure -> {
                    _loginState.value = LoginState.Error(
                        response.exception.message
                            ?: "Something went wrong!"
                    )
                }
            }
        }
    }
}