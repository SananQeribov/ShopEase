package com.example.shopease.di

import org.koin.dsl.module

val appModule = module {
includes(viewModelModule)
}