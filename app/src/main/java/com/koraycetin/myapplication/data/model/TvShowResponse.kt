package com.koraycetin.myapplication.data.model

data class TvShowResponse(
    val page: Int,
    val results: List<TvShow>,
    val totalPages: Int,
    val totalResults: Int
) 