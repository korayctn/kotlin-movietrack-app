package com.koraycetin.myapplication.data.remote

import com.koraycetin.myapplication.data.model.MovieResponse
import com.koraycetin.myapplication.data.model.TvShowResponse
import com.koraycetin.myapplication.util.Constants
import javax.inject.Inject

class MovieService @Inject constructor(
    private val api: MovieApi
) {
    suspend fun getPopularMovies(page: Int = 1): MovieResponse {
        return api.getPopularMovies(Constants.API_KEY, page)
    }

    suspend fun getPopularTvShows(page: Int = 1): TvShowResponse {
        return api.getPopularTvShows(Constants.API_KEY, page)
    }

    suspend fun getTrendingTvShows(page: Int = 1): TvShowResponse {
        return api.getPopularTvShows(Constants.API_KEY, page)
    }

    suspend fun getTrendingMovies(timeWindow: String = "day", page: Int = 1): MovieResponse {
        return api.getTrendingMovies(timeWindow, Constants.API_KEY, page)
    }

    suspend fun searchMovies(query: String, page: Int = 1): MovieResponse {
        return api.searchMovies("Bearer ${Constants.ACCESS_TOKEN}", query, page)
    }

    suspend fun searchTvShows(query: String, page: Int = 1): TvShowResponse {
        return api.searchTvShows("Bearer ${Constants.ACCESS_TOKEN}", query, page)
    }
} 