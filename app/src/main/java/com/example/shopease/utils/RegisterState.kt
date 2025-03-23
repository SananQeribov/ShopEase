package com.example.shopease.utils

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    class Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}