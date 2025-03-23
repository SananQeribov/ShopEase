package com.example.domain.di

import com.example.domain.usercase.CartUserCase
import com.example.domain.usercase.CategoryUseCase
import com.example.domain.usercase.LoginUserCase
import com.example.domain.usercase.ProductUseCase
import com.example.domain.usercase.SignUpUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { ProductUseCase(get()) }
    factory {CategoryUseCase(get()) }
    factory { CartUserCase(get()) }
    factory { LoginUserCase(get()) }
    factory { SignUpUseCase(get()) }
}
