package com.example.domain.usercase

import com.example.domain.repository.UserRepository

class SignUpUseCase(private val repository: UserRepository) {
    suspend fun execute (userName:String,password:String,name:String)=repository.register(userName,password,name)
}