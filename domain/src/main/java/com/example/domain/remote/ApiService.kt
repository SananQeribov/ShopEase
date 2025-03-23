package com.example.domain.remote

import com.example.domain.model.CartItemModel
import com.example.domain.model.CartModel
import com.example.domain.model.Product
import com.example.domain.model.UserModel
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.util.ResultWrapper

interface ApiService {
    suspend fun getProducts(category: String?): ResultWrapper<List<Product>>
    suspend fun getCategories ():ResultWrapper<List<String>>
    suspend fun addToProductToCart(request:AddCartRequestModel):ResultWrapper<CartModel>
    suspend fun login(email: String, password: String): ResultWrapper<UserModel>
    suspend fun register(
        email: String,
        password: String,
        name: String
    ): ResultWrapper<UserModel>
}



