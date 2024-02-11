package com.example.topmovie.retrofit


import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseUrl: String, context: Context?) : Retrofit {
        val cacheSize = 10 * 1024 * 1024
        context?.let {
            val cache = Cache(context.cacheDir, cacheSize.toLong())
            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

            if (retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
        return retrofit!!
    }
}