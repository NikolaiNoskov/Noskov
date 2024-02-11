package com.example.topmovie.data.entity.top_req

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.topmovie.data.entity.support.Country
import com.example.topmovie.data.entity.support.Genre

@Entity(tableName = "film")
data class FilmFromTop (
    @PrimaryKey
    val filmId : Int,
    val nameRu : String?=null,
    val nameEn : String?=null,
    val year : String?="",
    val filmLength : String?=null,
    @Ignore
    val countries : List<Country>,
    @Ignore
    var genres : MutableList<Genre>,
    var genresDB : String,
    @Ignore
    val rating : Number?=null,
    val ratingVoteCount	: Int?=null,
    val posterUrl : String,
    val posterUrlPreview : String?,
    var favourite:Boolean ?= null
) {
    constructor( filmId : Int,
                 nameRu : String?,
                 nameEn : String? ,
                 year : String? ,
                 filmLength : String?,
                 genresDB : String,
                 ratingVoteCount	: Int?,
                 posterUrl : String,
                 posterUrlPreview : String,
    ) : this(
        filmId,
        nameRu,
        nameEn,
        year,
        filmLength,
        listOf(),
        mutableListOf(),
        genresDB,
        0,
        ratingVoteCount,
        posterUrl,
        posterUrlPreview )
}
