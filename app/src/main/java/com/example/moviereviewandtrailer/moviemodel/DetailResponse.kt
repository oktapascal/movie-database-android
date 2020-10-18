package com.example.moviereviewandtrailer.moviemodel

data class DetailResponse (
    val id: Int?,
    val backdrop_path: String?,
    val poster_path: String?,
    val title: String?,
    val overview: String?,
    val release_date: String?,
    val genres: List<GenreModel>?,
    val vote_average: Float?
)