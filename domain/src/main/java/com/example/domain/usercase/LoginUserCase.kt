package com.example.domain.usercase

import com.example.domain.repository.UserRepository

class LoginUserCase(private val userRepository:UserRepository) {
    suspend fun execute (username:String,password:String) = userRepository.login(username,password)
}