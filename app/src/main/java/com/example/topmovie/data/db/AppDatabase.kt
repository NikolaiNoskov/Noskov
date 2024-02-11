package com.example.topmovie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.topmovie.data.db.dao.FilmDao
import com.example.topmovie.data.entity.top_req.FilmFromTop

@Database(entities = [FilmFromTop::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getFilmDao() : FilmDao
}