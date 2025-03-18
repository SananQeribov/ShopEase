package com.example.data.model
import com.example.domain.model.Product
import com.example.domain.model.Rating
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable



@Serializable
class ProductModel (
    val id: Long,
    @Contextual
    val rating: Rating,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val image: String
){

    fun toProduct() = Product(
        id = id,
        title = title,
        price = price,
        category = category,
        description = description,
        image = image,
        rating = rating
    )
}