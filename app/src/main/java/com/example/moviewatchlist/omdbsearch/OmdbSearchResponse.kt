package com.example.moviewatchlist.omdbsearch

data class OmdbSearchResponse(
    val Search: List<OmdbMovie>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)
