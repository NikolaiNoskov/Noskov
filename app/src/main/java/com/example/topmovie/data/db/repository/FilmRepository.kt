package com.example.topmovie.data.db.repository

import android.content.Context
import androidx.room.Room
import com.example.topmovie.data.db.AppDatabase
import com.example.topmovie.data.entity.top_req.FilmFromTop


class FilmRepository(context: Context) {
    private val db by lazy{
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }

    private val filmDao by lazy {
        db.getFilmDao()
    }


    suspend fun saveFilm(film: FilmFromTop) {
        filmDao.save(film)
    }

    suspend fun getAllFilms() :  List<FilmFromTop> {
        return filmDao.getAll()
    }

    companion object {
        private const val  DATABASE_NAME = "film.db"
    }

}