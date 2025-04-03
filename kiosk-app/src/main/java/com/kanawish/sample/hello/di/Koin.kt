package com.kanawish.sample.hello.di

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kanawish.sample.hello.model.KioskModel
import com.kanawish.sample.hello.navigation.MainNav
import com.kanawish.sample.hello.navigation.MainNavModel
import com.kanawish.sample.hello.navigation.NavEvent
import com.kanawish.sample.hello.navigation.TypeRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

fun previewModule() = module {
    // Add preview-specific dependencies here
    single<MainNav> { object : MainNav {
        override val mainNavEvents: SharedFlow<NavEvent>
            get() = TODO("Should not be called in preview")

        override fun nav(route: TypeRoute) {}
        override fun navUp() { }
        override fun navBack(route: TypeRoute, inclusive: Boolean) {}
        override fun navEvent(block: NavHostController.() -> Unit) {}
    } }
}

fun appModule() = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single<MainNav> { MainNavModel(get()) }

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

    singleOf(::KioskModel)

    single { androidContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    single { androidContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }

}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

