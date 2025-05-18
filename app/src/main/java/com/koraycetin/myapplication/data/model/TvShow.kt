package com.koraycetin.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tvshows")
data class TvShow(
    @PrimaryKey
    val id: Int,
    @SerializedName("name")
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    val popularity: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("media_type")
    val mediaType: String?,
    val adult: Boolean?,
    val isFavorite: Boolean = false,
    val isPopular: Boolean = false,
    val isTrending: Boolean = false
) 