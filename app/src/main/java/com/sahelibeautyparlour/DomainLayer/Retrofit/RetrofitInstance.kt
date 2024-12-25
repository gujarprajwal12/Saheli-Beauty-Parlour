package com.sahelibeautyparlour.DomainLayer.Retrofit

import com.sahelibeautyparlour.DataLayer.Constants
import com.sahelibeautyparlour.DomainLayer.GoogleDriveApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // OkHttpClient with custom configurations
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Write timeout
            .addInterceptor(HttpLoggingInterceptor().apply { // Add logging
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain -> // Add headers dynamically
                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer YOUR_TOKEN_HERE")
                    .addHeader("Authorization", "")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    // Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Base URL
            .client(okHttpClient) // Attach OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // JSON parser
            .build()
    }

    // API service instance
    val api: GoogleDriveApi by lazy {
        retrofit.create(GoogleDriveApi::class.java)
    }

    // Function to dynamically create a Retrofit instance with a different base URL
    fun createRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// Wrapper for handling API responses and errors
sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResponse<T> {
    return try {
        ApiResponse.Success(apiCall())
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "An error occurred")
    }
}
