package com.example.topmovie.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.topmovie.data.entity.top_req.FilmFromTop


@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(film: FilmFromTop)

    @Query("SELECT * FROM film")
    suspend  fun getAll() : List<FilmFromTop>

}