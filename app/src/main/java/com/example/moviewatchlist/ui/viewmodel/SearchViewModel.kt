package com.example.moviewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.model.UiMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_TIME = 300L
    }

    private var searchJob: Job? = null
    val searchResults = MutableLiveData<List<UiMovie>>()

    private val _liveMessage = MutableLiveData<String>()
    val liveMessage: LiveData<String> = _liveMessage

    fun search(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_TIME)

            val mappedResults = withContext(Dispatchers.IO) {
                val results = MovieRepository.searchMovies(query)
                results.map { movie ->
                    UiMovie(
                        imdbId = movie.imdbId,
                        title = movie.title,
                        type = movie.type,
                        year = movie.year,
                        watched = movie.watched,
                        poster = movie.poster
                    )
                }
            }

            searchResults.value = mappedResults
        }
    }

    fun addToWatchlist(movie: UiMovie) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    MovieRepository.addToWatchlist(movie)
                }

                if (result)
                {
                    _liveMessage.postValue("Movie added to watchlist")
                } else {
                    _liveMessage.postValue("Movie already in watchlist")
                }
            } catch (e: Exception) {
                _liveMessage.postValue("Movie not added to watchlist")
            }
        }
    }
}
