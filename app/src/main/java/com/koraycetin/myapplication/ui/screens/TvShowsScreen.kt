package com.koraycetin.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koraycetin.myapplication.data.model.TvShow
import com.koraycetin.myapplication.ui.components.SearchBar
import com.koraycetin.myapplication.ui.components.TvShowCard
import com.koraycetin.myapplication.ui.viewmodel.TvShowViewModel

@Composable
fun TvShowsScreen(
    onTvShowClick: (TvShow) -> Unit,
    viewModel: TvShowViewModel = hiltViewModel()
) {
    val trendingTvShows by viewModel.trendingTvShows.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    
    val listToShow = if (viewModel.isSearching) searchResults else trendingTvShows
    val isLoading = !viewModel.isSearching && listToShow.isEmpty() && !viewModel.hasLoadedTrendingOnce

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar is always visible at the top
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = { viewModel.searchTvShows() }
        )
        
        // Show header - either search results title or "Trending TV Shows"
        if (viewModel.isSearching) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.clearSearch() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Trending TV Shows"
                    )
                }
                Text(
                    text = "Search Results for '${viewModel.searchQuery}'",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Header for Trending TV Shows
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trending TV Shows",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Show loading, empty results, or content
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.isSearching && searchResults.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No results found for '${viewModel.searchQuery}'",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try different keywords or check your spelling",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = listToShow,
                            key = { it.id }
                        ) { tvShow ->
                            TvShowCard(
                                tvShow = tvShow,
                                onTvShowClick = onTvShowClick,
                                onFavoriteClick = { viewModel.toggleFavorite(it) }
                            )
                        }
                    }
                }
            }
        }
    }
} 