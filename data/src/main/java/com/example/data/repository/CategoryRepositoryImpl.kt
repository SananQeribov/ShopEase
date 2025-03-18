package com.example.data.repository

import com.example.domain.remote.ApiService
import com.example.domain.repository.CategoryRepository
import com.example.domain.util.ResultWrapper

class CategoryRepositoryImpl(val apiService:ApiService):CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<List<String>> {
        return apiService.getCategories()
    }
}