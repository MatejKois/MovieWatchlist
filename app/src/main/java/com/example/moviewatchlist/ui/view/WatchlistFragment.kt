package com.example.moviewatchlist.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.ui.adapters.WatchlistAdapter
import com.example.moviewatchlist.ui.viewmodel.WatchlistViewModel
import com.google.android.material.snackbar.Snackbar

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private lateinit var viewModel: WatchlistViewModel
    private lateinit var adapter: WatchlistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]

        adapter = WatchlistAdapter { movie, watched ->
            viewModel.setWatched(movie.imdbId, watched)
            viewModel.loadWatchlist()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWatchlist)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = adapter.currentList[position]

                viewModel.removeFromWatchlist(movie.imdbId)

                Snackbar.make(
                    recyclerView,
                    "Movie removed",
                    Snackbar.LENGTH_LONG
                ).setAction("UNDO") {
                    viewModel.addToWatchlist(movie)
                }.show()
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
        }

        viewModel.loadWatchlist()
    }
}
