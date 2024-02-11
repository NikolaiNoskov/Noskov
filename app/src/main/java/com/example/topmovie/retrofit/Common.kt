package com.example.topmovie.retrofit

import android.content.Context

object Common {

    private val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"
    fun retrofitService(context: Context?): RetrofitService {
        return RetrofitClient.getClient(BASE_URL, context).create(RetrofitService::class.java)
    }
}