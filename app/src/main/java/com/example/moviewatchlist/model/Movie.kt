package com.example.moviewatchlist.model

data class Movie(
    val imdbId: String,
    val title: String,
    val year: Int,
    val type: String,
    val watched: Boolean,
    val poster: ByteArray?
)
