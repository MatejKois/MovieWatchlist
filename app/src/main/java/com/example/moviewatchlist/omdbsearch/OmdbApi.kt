package com.example.moviewatchlist.omdbsearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET(".")
    fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): Call<OmdbSearchResponse>
}
