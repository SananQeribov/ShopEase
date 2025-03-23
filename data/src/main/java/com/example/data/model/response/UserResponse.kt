package com.example.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
@SerialName("email")
    val email: String="",
    val id: Int,
@SerialName("password")
    val password: String="",
@SerialName("username")
    val username: String=""
) {
    fun toDomainModel() = com.example.domain.model.UserModel(
        id = id,
        username = username,
        email = email,
        password =password
    )
}