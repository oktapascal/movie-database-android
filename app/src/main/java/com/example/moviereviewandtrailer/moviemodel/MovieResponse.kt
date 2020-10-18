package com.example.moviereviewandtrailer.moviemodel

data class MovieResponse (
    val total_pages: Int?,
    val results: List<MovieModel>
)