package com.example.simkader_236.repositori

import android.app.Application
import android.content.Context
import com.example.simkader_236.apiservice.ServiceApiKader
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

// Kontrak Container murni untuk MySQL
interface AppContainer {
    val repositoriDataKader: RepositoriDataKader
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // --- KONFIGURASI MYSQL (RETROFIT) ---
    // Gunakan IP Laptop Anda jika menggunakan HP fisik, atau 10.0.2.2 untuk emulator
    private val baseUrl = "http://10.17.71.102/simkader/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    private val serviceApiKader: ServiceApiKader by lazy {
        retrofit.create(ServiceApiKader::class.java)
    }

    // Hanya menggunakan JaringanRepositori (MySQL)
    override val repositoriDataKader: RepositoriDataKader by lazy {
        JaringanRepositoriDataKader(serviceApiKader)
    }
}

// Class Application sebagai Entry Point Utama
class AplikasiDataKader : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}