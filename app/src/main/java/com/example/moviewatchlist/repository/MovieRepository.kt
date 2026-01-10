package com.example.moviewatchlist.repository

import android.content.Context
import com.example.moviewatchlist.database.WatchlistDbHelper
import com.example.moviewatchlist.model.Movie
import com.example.moviewatchlist.ui.model.UiMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

object MovieRepository {
    const val MIN_SEARCH_LENGTH = 3
    const val API_KEY = "34df5d0f"
    const val UNKNOWN = "N/A"

    private lateinit var dbHelper: WatchlistDbHelper
    private val okHttpClient = OkHttpClient()

    fun init(context: Context)
    {
        dbHelper = WatchlistDbHelper(context.applicationContext)
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

    fun addToWatchlist(movie: UiMovie): Boolean {
        return dbHelper.insert(toMovie(movie))
    }

    fun setWatched(imdbId: String, watched: Boolean) {
        dbHelper.setWatched(imdbId, watched)
    }

    fun getAllWatchlistMovies(): List<UiMovie> {
        return dbHelper.getAllMovies().map { toMovie(it) }
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

    private fun toMovie(movie: UiMovie): Movie {
        return Movie(
            imdbId = movie.imdbId,
            title = movie.title,
            year = movie.year,
            type = movie.type,
            watched = movie.watched,
            poster = movie.poster
        )
    }

    private fun toMovie(movie: Movie): UiMovie {
        return UiMovie(
            imdbId = movie.imdbId,
            title = movie.title,
            year = movie.year,
            type = movie.type,
            watched = movie.watched,
            poster = movie.poster
        )
    }
}
