package com.example.moviewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.model.UiMovie
import kotlinx.coroutines.launch

class WatchlistViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<UiMovie>>()
    val movies: LiveData<List<UiMovie>> = _movies

    fun loadWatchlist() {
        viewModelScope.launch {
            refreshWatchlist()
        }
    }

    fun setWatched(imdbId: String, watched: Boolean) {
        viewModelScope.launch {
            MovieRepository.setWatched(imdbId, watched)
            refreshWatchlist()
        }
    }

    fun removeFromWatchlist(imdbId: String) {
        viewModelScope.launch {
            MovieRepository.removeFromWatchlist(imdbId)
            refreshWatchlist()
        }
    }

    fun addToWatchlist(movie: UiMovie) {
        viewModelScope.launch {
            MovieRepository.addToWatchlist(movie)
            refreshWatchlist()
        }
    }

    private suspend fun refreshWatchlist() {
        val movies = MovieRepository.getAllWatchlistMovies()
        _movies.postValue(movies)
    }
}
