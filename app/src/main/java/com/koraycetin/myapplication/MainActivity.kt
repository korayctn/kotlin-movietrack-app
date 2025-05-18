package com.koraycetin.myapplication

import MovieDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.koraycetin.myapplication.ui.screens.MoviesScreen
import com.koraycetin.myapplication.ui.screens.TvShowsScreen
import com.koraycetin.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.mutableIntStateOf
import android.net.Uri
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import com.google.gson.Gson
import androidx.navigation.NavType
import com.koraycetin.myapplication.ui.screens.TvShowDetailScreen
import com.koraycetin.myapplication.ui.screens.FavoritesScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val gson = remember { Gson() }

    val tabItems = listOf(
        TabItem("movies", "Movies", Icons.Filled.Movie),
        TabItem("tvshows", "TV Shows", Icons.Filled.Tv),
        TabItem("favorites", "Favorites", Icons.Filled.Star)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabItems.forEachIndexed { index, tabItem ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            if (navController.currentDestination?.route != tabItem.route) {
                                navController.navigate(tabItem.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(tabItem.icon, contentDescription = tabItem.label) },
                        label = { Text(tabItem.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "movies",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("movies") {
                MoviesScreen(
                    onMovieClick = { movie ->
                        val movieJson = Uri.encode(gson.toJson(movie))
                        navController.navigate("movieDetail/$movieJson")
                    }
                )
            }
            composable("tvshows") {
                TvShowsScreen(
                    onTvShowClick = { tvShow ->
                        val tvShowJson = Uri.encode(gson.toJson(tvShow))
                        navController.navigate("tvShowDetail/$tvShowJson")
                    }
                )
            }
            composable("favorites") {
                FavoritesScreen()
            }
            composable(
                "movieDetail/{movie}",
                arguments = listOf(navArgument("movie") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieJson = backStackEntry.arguments?.getString("movie")
                val movie = gson.fromJson(movieJson, com.koraycetin.myapplication.data.model.Movie::class.java)
                MovieDetailScreen(movie)
            }
            composable(
                "tvShowDetail/{tvShow}",
                arguments = listOf(navArgument("tvShow") { type = NavType.StringType })
            ) { backStackEntry ->
                val tvShowJson = backStackEntry.arguments?.getString("tvShow")
                val tvShow = gson.fromJson(tvShowJson, com.koraycetin.myapplication.data.model.TvShow::class.java)
                TvShowDetailScreen(
                    tvShow = tvShow,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

data class TabItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)


