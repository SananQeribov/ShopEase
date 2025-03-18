package com.example.domain.usercase

import com.example.domain.model.CartItemModel
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.repository.CartRepository
import com.example.domain.util.ResultWrapper

class CartUserCase(private val repository:CartRepository) {
suspend fun  execute (requestModel: AddCartRequestModel) = repository.addToProductToCart(requestModel)
}