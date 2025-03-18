package com.example.shopease.utils

import com.example.domain.model.Product

sealed class HomeScreenUIEvents {
    data object Loading : HomeScreenUIEvents()
    data class Success(
        val featured: List<Product>,
        val popularProduct: List<Product>,
        val categories: List<String>,
    ) : HomeScreenUIEvents()

    data class Error(val message: String) : HomeScreenUIEvents()
}