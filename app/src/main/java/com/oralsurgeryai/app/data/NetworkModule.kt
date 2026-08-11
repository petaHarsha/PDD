package com.oralsurgeryai.app.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private var currentIp: String = "10.0.2.2" // DEFAULT TO EMULATOR HOST (LOCAL), DISCOVERY WILL OVERRIDE
    private var _apiService: ApiService? = null

    val apiService: ApiService
        get() {
            if (_apiService == null) {
                _apiService = buildRetrofit(currentIp).create(ApiService::class.java)
            }
            return _apiService!!
        }

    fun updateIp(newIp: String) {
        if (newIp != currentIp) {
            Log.i("NetworkModule", "Updating Server IP to: $newIp")
            currentIp = newIp
            _apiService = buildRetrofit(currentIp).create(ApiService::class.java)
        }
    }

    private fun buildRetrofit(ip: String): Retrofit {
        val baseUrl = "http://$ip:8000/"
        
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(600, TimeUnit.SECONDS)

        if (com.oralsurgeryai.app.BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        val client = clientBuilder.build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
