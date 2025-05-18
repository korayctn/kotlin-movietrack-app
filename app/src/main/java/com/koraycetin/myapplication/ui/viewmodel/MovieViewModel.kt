package com.koraycetin.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koraycetin.myapplication.data.model.Movie
import com.koraycetin.myapplication.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults

    var searchQuery by mutableStateOf("")
        private set

    var isSearching by mutableStateOf(false)
        private set

    var hasLoadedPopularOnce by mutableStateOf(false)
        private set
    var hasLoadedTrendingOnce by mutableStateOf(false)
        private set

    init {
        refreshMovies()
        loadMovies()
        loadFavoriteMovies()
        loadPopularMovies()
        loadTrendingMovies()
        viewModelScope.launch {
            _popularMovies.value = repository.getPopularMovies()
            _trendingMovies.value = repository.getTrendingMovies()
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            repository.getAllMovies()
                .catch { e -> e.printStackTrace() }
                .collect { movies ->
                    _movies.value = movies
                }
        }
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovies()
                .catch { e -> e.printStackTrace() }
                .collect { movies ->
                    _favoriteMovies.value = movies
                }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            repository.getAllMovies()
                .catch { e -> e.printStackTrace() }
                .collect { movies ->
                    _popularMovies.value = movies.filter { it.isPopular }
                    if (movies.isNotEmpty()) hasLoadedPopularOnce = true
                }
        }
    }

    private fun loadTrendingMovies() {
        viewModelScope.launch {
            repository.getAllMovies()
                .catch { e -> e.printStackTrace() }
                .collect { movies ->
                    _trendingMovies.value = movies.filter { it.isTrending }
                    if (movies.isNotEmpty()) hasLoadedTrendingOnce = true
                }
        }
    }

    fun refreshMovies() {
        viewModelScope.launch {
            repository.refreshMovies()
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.toggleFavorite(movie)
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchMovies() {
        if (searchQuery.isBlank()) {
            _searchResults.value = emptyList()
            isSearching = false
            return
        }

        isSearching = true
        viewModelScope.launch {
            try {
                val results = repository.searchMovies(searchQuery)
                _searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    fun clearSearch() {
        searchQuery = ""
        _searchResults.value = emptyList()
        isSearching = false
    }
} 