package com.example.domain.usercase

import com.example.domain.repository.CategoryRepository

class CategoryUseCase(private val repository: CategoryRepository) {
    suspend fun execute() = repository.getCategories()
}