package com.example.domain.usercase

import com.example.domain.repository.ProductRepository

class ProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(category: String?) = repository.getProducts(category)
}
