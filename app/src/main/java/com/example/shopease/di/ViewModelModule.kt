package com.example.shopease.di

import com.example.shopease.viewModel.HomeViewModel
import com.example.shopease.viewModel.ProductDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val viewModelModule = module {
  viewModel {
        HomeViewModel(get(),get())

    }
    single { ProductDetailViewModel(get()) }

}