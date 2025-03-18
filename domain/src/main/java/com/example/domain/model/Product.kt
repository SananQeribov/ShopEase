package com.example.domain.model

import kotlinx.serialization.Serializable


data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val rating: com.example.domain.model.Rating,
    val category: String,
    val description: String,
    val image: String
) {
    val priceString: String
        get() = "$$price"

}

