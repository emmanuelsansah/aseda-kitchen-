package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SupabaseClient {

    private const val DEFAULT_URL = "https://vctjxlncglyisbqlidon.supabase.co"
    private const val DEFAULT_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZjdGp4bG5jZ2x5aXNicWxpZG9uIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTQyMDgwNzgsImV4cCI6MjEwOTc4NDA3OH0.A9fFVjXiBJIL8UweTduFimkJMcYh9MjgKsLAyE60ygA"

    val baseUrl: String
        get() {
            val url = try { BuildConfig.SUPABASE_URL } catch (e: Exception) { "" }
            return if (url.isNotBlank() && !url.contains("MY_")) url else DEFAULT_URL
        }

    val anonKey: String
        get() {
            val key = try { BuildConfig.SUPABASE_ANON_KEY } catch (e: Exception) { "" }
            return if (key.isNotBlank() && !key.contains("MY_")) key else DEFAULT_ANON_KEY
        }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("apiKey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val apiService: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SupabaseApiService::class.java)
    }
}
