package com.koraycetin.myapplication.data.repository

import com.koraycetin.myapplication.data.local.MovieDao
import com.koraycetin.myapplication.data.model.Movie
import com.koraycetin.myapplication.data.remote.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao
) {
    fun getAllMovies(): Flow<List<Movie>> = movieDao.getAllMovies()
    fun getFavoriteMovies(): Flow<List<Movie>> = movieDao.getFavoriteMovies()

    suspend fun getPopularMovies(): List<Movie> {
        val movies = movieService.getPopularMovies().results.map { it.copy(isPopular = true) }
        movieDao.insertMovies(movies)
        return movies
    }

    suspend fun getTrendingMovies(): List<Movie> {
        val movies = movieService.getTrendingMovies().results.map { it.copy(isTrending = true) }
        movieDao.insertMovies(movies)
        return movies
    }

    suspend fun searchMovies(query: String): List<Movie> {
        val searchResults = movieService.searchMovies(query).results
        
        // Get existing movies from database to preserve favorite status
        val movieIds = searchResults.map { movie -> movie.id }
        val existingMovies = movieDao.getMoviesByIds(movieIds)
        val existingMoviesMap = existingMovies.associateBy { movie -> movie.id }
        
        // Merge search results with existing data to preserve favorite status
        val moviesWithFavoriteStatus = searchResults.map { movie ->
            val existingMovie = existingMoviesMap[movie.id]
            movie.copy(isFavorite = existingMovie?.isFavorite ?: false)
        }
        
        // Save search results to database
        movieDao.insertMovies(moviesWithFavoriteStatus)
        
        return moviesWithFavoriteStatus
    }

    suspend fun refreshMovies() {
        val response = movieService.getPopularMovies()
        movieDao.insertMovies(response.results)
    }

    suspend fun toggleFavorite(movie: Movie) {
        movieDao.updateMovie(movie.copy(isFavorite = !movie.isFavorite))
    }
} 