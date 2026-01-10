package com.example.moviewatchlist.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.repository.MovieRepository
import com.example.moviewatchlist.ui.adapters.WatchlistAdapter
import com.example.moviewatchlist.ui.viewmodel.WatchlistViewModel

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private lateinit var viewModel: WatchlistViewModel
    private lateinit var adapter: WatchlistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]

        adapter = WatchlistAdapter { movie, watched ->
            MovieRepository.setWatched(movie.imdbId, watched)
            viewModel.loadWatchlist()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWatchlist)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
        }

        viewModel.loadWatchlist()
    }
}
