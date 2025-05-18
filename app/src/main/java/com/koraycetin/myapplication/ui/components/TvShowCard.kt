package com.koraycetin.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.koraycetin.myapplication.data.model.TvShow
import com.koraycetin.myapplication.util.Constants

@Composable
fun TvShowCard(
    tvShow: TvShow,
    onTvShowClick: (TvShow) -> Unit,
    onFavoriteClick: (TvShow) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTvShowClick(tvShow) }
    ) {
        Box {
            AsyncImage(
                model = "${Constants.IMAGE_BASE_URL}${tvShow.posterPath}",
                contentDescription = tvShow.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            
            IconButton(
                onClick = { onFavoriteClick(tvShow) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (tvShow.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (tvShow.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = tvShow.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = tvShow.overview,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
    }
} 