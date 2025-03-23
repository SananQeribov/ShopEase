package com.example.data.repository

import com.example.domain.remote.ApiService
import com.example.domain.repository.UserRepository

class UserRepositoryImpl(private val networkService: ApiService) : UserRepository {
    override suspend fun login(email: String, password: String) =
        networkService.login(email, password)

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ) = networkService.register(email, password, name)

}