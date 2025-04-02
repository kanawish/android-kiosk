package com.kanawish.sample.hello.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import retrofit2.Retrofit

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule())
    }
}

fun appModule() = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            // TODO take in consideration enableNetworkLogs parameter
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.HEADERS)
                }
            )
            .build()
    }

    single<Json> { createJson() }
    single<Gson> { GsonBuilder().setLenient().create() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://ramp-interview.netlify.app/")
            .client(get())
            .build()
    }

}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

