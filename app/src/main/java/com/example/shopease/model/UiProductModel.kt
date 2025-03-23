package com.example.shopease.model

import android.os.Parcelable
import com.example.domain.model.Product
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UiProductModel(
    val id: Long,
    val title: String,
    val price: Double,
    val category:String,
    val description: String,
    val image: String,
    var count:Int =1
) :Parcelable {
companion object{
        fun fromProduct(product: Product) = UiProductModel(
            id = product.id,
            title = product.title,
            price = product.price,
            category = product.category,
            image = product.image,
            description =product.description
        )
    }}

