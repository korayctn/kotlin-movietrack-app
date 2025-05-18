package com.koraycetin.myapplication.data.repository

import com.koraycetin.myapplication.data.local.TvShowDao
import com.koraycetin.myapplication.data.model.TvShow
import com.koraycetin.myapplication.data.remote.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val movieService: MovieService,
    private val tvShowDao: TvShowDao
) {
    fun getAllTvShows(): Flow<List<TvShow>> = tvShowDao.getAllTvShows()
    
    fun getFavoriteTvShows(): Flow<List<TvShow>> = tvShowDao.getFavoriteTvShows()

    suspend fun deleteAllTvShows() {
        tvShowDao.deleteAllTvShows()
    }

    suspend fun insertTvShows(tvShows: List<TvShow>) {
        tvShowDao.insertTvShows(tvShows)
    }

    suspend fun getPopularTvShows(): List<TvShow> {
        return try {
            movieService.getPopularTvShows().results
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTrendingTvShows(): List<TvShow> {
        return try {
            movieService.getTrendingTvShows().results
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Search for TV shows and save results to database
    suspend fun findTvShows(query: String): List<TvShow> {
        return try {
            val searchResults = movieService.searchTvShows(query).results
            
            // Get existing TV shows from database to preserve favorite status
            val tvShowIds = searchResults.map { tvShow -> tvShow.id }
            val existingTvShows = tvShowDao.getTvShowsByIds(tvShowIds)
            val existingTvShowsMap = existingTvShows.associateBy { tvShow -> tvShow.id }
            
            // Merge search results with existing data to preserve favorite status
            val tvShowsWithFavoriteStatus = searchResults.map { tvShow ->
                val existingTvShow = existingTvShowsMap[tvShow.id]
                tvShow.copy(isFavorite = existingTvShow?.isFavorite ?: false)
            }
            
            // Save search results to database
            tvShowDao.insertTvShows(tvShowsWithFavoriteStatus)
            
            tvShowsWithFavoriteStatus
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun toggleFavorite(tvShow: TvShow) {
        tvShowDao.updateTvShow(tvShow.copy(isFavorite = !tvShow.isFavorite))
    }
} 