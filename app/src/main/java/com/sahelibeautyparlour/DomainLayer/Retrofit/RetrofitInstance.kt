package com.sahelibeautyparlour.DomainLayer.Retrofit

import com.sahelibeautyparlour.DataLayer.Constants
import com.sahelibeautyparlour.DomainLayer.GoogleDriveApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GoogleDriveApi by lazy {
        retrofit.create(GoogleDriveApi::class.java)
    }
}
