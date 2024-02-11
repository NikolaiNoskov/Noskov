package com.example.topmovie.retrofit

import com.example.topmovie.data.entity.byId_req.Film
import com.example.topmovie.data.entity.top_req.FilmFromTopResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitService {
    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("top?type=TOP_100_POPULAR_FILMS")
    fun getTop100Films(

    ): Call<FilmFromTopResponse>


    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("{id}")
    fun getFilmById(
         @Path("id") id : String
    ) : Call<Film>
}