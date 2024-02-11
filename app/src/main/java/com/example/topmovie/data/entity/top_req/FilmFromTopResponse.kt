package com.example.topmovie.data.entity.top_req

data class FilmFromTopResponse(
    val pageCount: Int,
    val films: List<FilmFromTop>?=null,
)
