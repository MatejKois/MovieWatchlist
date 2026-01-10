package com.example.moviewatchlist.repository

import android.content.Context
import com.example.moviewatchlist.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

object MovieRepository {
    const val MIN_SEARCH_LENGTH = 3
    const val API_KEY = "34df5d0f"
    const val UNKNOWN = "N/A"

    private val okHttpClient = OkHttpClient()

    fun init(context: Context) {
    }

    suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        if (query.isBlank() || query.length < MIN_SEARCH_LENGTH) {
            return@withContext emptyList()
        }

        try {
            val response = OmdbClient.api.searchMovies(API_KEY, query).execute()
            if (response.isSuccessful) {
                val body = response.body()
                body?.Search?.map { omdbMovie ->
                    val posterBytes = omdbMovie.Poster?.takeIf { it != UNKNOWN }?.let { url ->
                        downloadImage(url)
                    }

                    Movie(
                        imdbId = omdbMovie.imdbID,
                        title = omdbMovie.Title,
                        year = omdbMovie.Year.toIntOrNull() ?: 0,
                        type = omdbMovie.Type,
                        watched = false,
                        poster = posterBytes
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun downloadImage(url: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val request = okhttp3.Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                response.body?.bytes()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
