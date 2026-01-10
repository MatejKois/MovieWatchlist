package com.example.moviewatchlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.model.UiMovie

class SearchViewModel : ViewModel() {

    suspend fun search(query: String): List<UiMovie> {
        return MovieRepository.searchMovies(query).map {
            UiMovie(
                imdbId = it.imdbId,
                title = it.title,
                type = it.type,
                year = it.year,
                watched = it.watched,
                poster = it.poster
            )
        }
    }
}
