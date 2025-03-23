package com.example.shopease.utils

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    class Success : LoginState()
    data class Error(val message: String) : LoginState()
}