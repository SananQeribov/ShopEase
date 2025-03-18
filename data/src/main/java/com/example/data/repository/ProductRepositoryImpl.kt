package com.example.data.repository

import com.example.domain.model.Product
import com.example.domain.remote.ApiService
import com.example.domain.repository.ProductRepository
import com.example.domain.util.ResultWrapper

class ProductRepositoryImpl(val network:ApiService):  ProductRepository {

    override suspend fun getProducts(category: String?): ResultWrapper<List<Product>>{
        return network.getProducts(category)
    }



}