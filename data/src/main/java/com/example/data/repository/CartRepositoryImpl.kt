package com.example.data.repository

import com.example.domain.model.CartModel
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.remote.ApiService
import com.example.domain.repository.CartRepository
import com.example.domain.util.ResultWrapper

class CartRepositoryImpl(val networkService:ApiService):CartRepository {
    override suspend fun addToProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> {
        return  networkService.addToProductToCart(request)
    }



}