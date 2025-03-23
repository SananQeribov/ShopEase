package com.example.domain.repository

import com.example.domain.model.UserModel
import com.example.domain.util.ResultWrapper

interface UserRepository {
    suspend fun login (email:String,password:String):ResultWrapper<UserModel>
    suspend fun register (email:String,password:String,name:String):ResultWrapper<UserModel>
}