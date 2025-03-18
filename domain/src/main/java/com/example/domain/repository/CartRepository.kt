package com.example.domain.repository

import com.example.domain.model.CartItemModel
import com.example.domain.model.CartModel
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.util.ResultWrapper

interface CartRepository {
    suspend fun addToProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>
}