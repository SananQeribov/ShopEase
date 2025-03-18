package com.example.data.di

import com.example.data.remote.ApiServiceImpl
import com.example.domain.remote.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                   ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000 // 60 saniyə
                connectTimeoutMillis = 30_000 // 30 saniyə
                socketTimeoutMillis = 30_000 // 30 saniyə
            }

        }
    }
    single<ApiService> {
     ApiServiceImpl(get())
    }
}