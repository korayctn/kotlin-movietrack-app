package com.koraycetin.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koraycetin.myapplication.data.model.TvShow
import com.koraycetin.myapplication.data.repository.TvShowRepository
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
class TvShowViewModel @Inject constructor(
    private val repository: TvShowRepository
) : ViewModel() {

    private val _tvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val tvShows: StateFlow<List<TvShow>> = _tvShows

    private val _favoriteTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val favoriteTvShows: StateFlow<List<TvShow>> = _favoriteTvShows

    private val _popularTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val popularTvShows: StateFlow<List<TvShow>> = _popularTvShows

    private val _trendingTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val trendingTvShows: StateFlow<List<TvShow>> = _trendingTvShows

    private val _searchResults = MutableStateFlow<List<TvShow>>(emptyList())
    val searchResults: StateFlow<List<TvShow>> = _searchResults

    var searchQuery by mutableStateOf("")
        private set

    var isSearching by mutableStateOf(false)
        private set

    var hasLoadedPopularOnce by mutableStateOf(false)
        private set
    var hasLoadedTrendingOnce by mutableStateOf(false)
        private set

    init {
        loadTvShows()
        loadFavoriteTvShows()
        loadPopularTvShows()
        loadTrendingTvShows()
        refreshTvShows()
    }

    private fun loadTvShows() {
        viewModelScope.launch {
            repository.getAllTvShows()
                .catch { e -> e.printStackTrace() }
                .collect { tvShows ->
                    _tvShows.value = tvShows
                }
        }
    }

    private fun loadFavoriteTvShows() {
        viewModelScope.launch {
            repository.getFavoriteTvShows()
                .catch { e -> e.printStackTrace() }
                .collect { tvShows ->
                    _favoriteTvShows.value = tvShows
                }
        }
    }

    private fun loadPopularTvShows() {
        viewModelScope.launch {
            repository.getAllTvShows()
                .catch { e -> e.printStackTrace() }
                .collect { tvShows ->
                    _popularTvShows.value = tvShows.filter { it.isPopular }
                    if (tvShows.isNotEmpty()) hasLoadedPopularOnce = true
                }
        }
    }

    private fun loadTrendingTvShows() {
        viewModelScope.launch {
            repository.getAllTvShows()
                .catch { e -> e.printStackTrace() }
                .collect { tvShows ->
                    _trendingTvShows.value = tvShows.filter { it.isTrending }
                    if (tvShows.isNotEmpty()) hasLoadedTrendingOnce = true
                }
        }
    }

    fun refreshTvShows() {
        viewModelScope.launch {
            try {
                repository.deleteAllTvShows()
                val popular = repository.getPopularTvShows().map { it.copy(isPopular = true) }
                repository.insertTvShows(popular)
                val trending = repository.getTrendingTvShows().map { it.copy(isTrending = true) }
                repository.insertTvShows(trending)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(tvShow: TvShow) {
        viewModelScope.launch {
            repository.toggleFavorite(tvShow)
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchTvShows() {
        if (searchQuery.isBlank()) {
            _searchResults.value = emptyList()
            isSearching = false
            return
        }

        isSearching = true
        viewModelScope.launch {
            try {
                val results = repository.findTvShows(searchQuery)
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