package com.example.moviewatchlist.ui.model

data class UiMovie(
    val imdbId: String,
    val title: String,
    val year: Int,
    val type: String,
    val watched: Boolean,
    val poster: ByteArray?
)
