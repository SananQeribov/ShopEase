package com.example.shopease.utils

import com.example.domain.model.CartItemModel
import com.example.shopease.model.UiProductModel

sealed class CartEvent {
    data object Loading : CartEvent()
    data class Success(val message: List<UiProductModel>) : CartEvent()
    data class Error(val message: String) : CartEvent()
}