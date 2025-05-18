package com.koraycetin.myapplication.data.local

import androidx.room.*
import com.koraycetin.myapplication.data.model.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {
    @Query("SELECT * FROM tvshows")
    fun getAllTvShows(): Flow<List<TvShow>>

    @Query("SELECT * FROM tvshows WHERE isFavorite = 1")
    fun getFavoriteTvShows(): Flow<List<TvShow>>

    @Query("SELECT * FROM tvshows WHERE id IN (:tvShowIds)")
    suspend fun getTvShowsByIds(tvShowIds: List<Int>): List<TvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShow: TvShow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShows(tvShows: List<TvShow>)

    @Update
    suspend fun updateTvShow(tvShow: TvShow)

    @Delete
    suspend fun deleteTvShow(tvShow: TvShow)

    @Query("DELETE FROM tvshows")
    suspend fun deleteAllTvShows()
} 