package com.example.shopease.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.request.RegisterRequest
import com.example.domain.usercase.SignUpUseCase
import com.example.domain.util.ResultWrapper
import com.example.shopease.ShopperSession
import com.example.shopease.utils.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase:SignUpUseCase
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState

    fun register(email: String, password: String, name: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            when (val response = registerUseCase.execute(email, password, name)) {
                is ResultWrapper.Success -> {
                    ShopperSession.storeUser(response.value)
                    _registerState.value = RegisterState.Success()
                }

                is ResultWrapper.Failure -> {
                    _registerState.value = RegisterState.Error(
                        response.exception.message
                            ?: "Something went wrong!"
                    )
                }
            }
        }
    }
}