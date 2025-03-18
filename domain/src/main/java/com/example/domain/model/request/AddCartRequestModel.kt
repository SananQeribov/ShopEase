package com.example.domain.model.request

data class AddCartRequestModel(
    val productId: Long,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val userId: Int, // Link cart item to the user
)