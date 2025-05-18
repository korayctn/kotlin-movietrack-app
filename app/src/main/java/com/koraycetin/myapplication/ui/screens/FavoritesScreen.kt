package com.koraycetin.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koraycetin.myapplication.ui.components.MovieCard
import com.koraycetin.myapplication.ui.components.TvShowCard
import com.koraycetin.myapplication.ui.viewmodel.MovieViewModel
import com.koraycetin.myapplication.ui.viewmodel.TvShowViewModel

@Composable
fun FavoritesScreen(
    movieViewModel: MovieViewModel = hiltViewModel(),
    tvShowViewModel: TvShowViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val favoriteMovies by movieViewModel.favoriteMovies.collectAsState()
    val favoriteTvShows by tvShowViewModel.favoriteTvShows.collectAsState()

    val tabTitles = listOf("Movies", "TV Shows")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> {
                    if (favoriteMovies.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No favorite movies yet")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = favoriteMovies,
                                key = { it.id }
                            ) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onMovieClick = {},
                                    onFavoriteClick = { movieViewModel.toggleFavorite(it) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    if (favoriteTvShows.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No favorite TV shows yet")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = favoriteTvShows,
                                key = { it.id }
                            ) { tvShow ->
                                TvShowCard(
                                    tvShow = tvShow,
                                    onTvShowClick = {},
                                    onFavoriteClick = { tvShowViewModel.toggleFavorite(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 