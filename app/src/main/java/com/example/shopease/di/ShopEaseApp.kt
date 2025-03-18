package com.example.shopease.di

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopEaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopEaseApp)
            modules (listOf(appModule, dataModule, domainModule))

        }
    }
}