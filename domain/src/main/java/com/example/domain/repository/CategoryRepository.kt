package com.example.domain.repository

import com.example.domain.util.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories ():ResultWrapper<List<String>>
}