package com.example.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val emil: String,
    val password:String,
    val username:String

    )
