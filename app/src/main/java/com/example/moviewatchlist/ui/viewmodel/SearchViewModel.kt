package com.example.moviewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.model.UiMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

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

    fun addToWatchlist(movie: UiMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!MovieRepository.addToWatchlist(movie))
                {
                    _errorMessage.postValue("Movie already in watchlist")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Movie not added to watchlist")
            }
        }
    }
}
