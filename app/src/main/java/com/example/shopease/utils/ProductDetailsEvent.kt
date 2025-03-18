package com.example.shopease.utils

import com.example.domain.model.CartModel

sealed class ProductDetailsEvent {
    data object Loading : ProductDetailsEvent()
    data object Nothing : ProductDetailsEvent()
    data class Success(val message: String) : ProductDetailsEvent()
    data class Error(val message: String) : ProductDetailsEvent()
}