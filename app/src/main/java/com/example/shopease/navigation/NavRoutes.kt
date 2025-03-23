package com.example.shopease.navigation

import com.example.data.model.ProductModel
import com.example.shopease.model.UiProductModel
import kotlinx.serialization.Serializable


@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object ProfileScreen

@Serializable
data class ProductDetails(val product: UiProductModel)

@Serializable
object CardBasket