package com.example.shopease.utils

import com.example.domain.model.Product
import com.example.shopease.model.UiProductModel

sealed class BasketScreenUIEvents {
    data object  Initial:BasketScreenUIEvents()
    data object Loading : BasketScreenUIEvents()
    data class Success(
        val cards: List<UiProductModel>,

    ) : BasketScreenUIEvents()

    data class Error(val message: String) : BasketScreenUIEvents()
}