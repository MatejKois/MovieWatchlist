package com.example.moviewatchlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.model.UiMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<UiMovie>>()
    val movies: LiveData<List<UiMovie>> = _movies

    fun loadWatchlist() {
        viewModelScope.launch {
            val movies = withContext(Dispatchers.IO) { MovieRepository.getAllWatchlistMovies() }
            _movies.value = movies
        }
    }
}
