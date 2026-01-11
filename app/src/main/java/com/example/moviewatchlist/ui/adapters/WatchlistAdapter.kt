package com.example.moviewatchlist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.ui.model.UiMovie

class WatchlistAdapter(
    private val onWatchedChanged: (UiMovie, Boolean) -> Unit
) : ListAdapter<UiMovie, WatchlistAdapter.WatchlistViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_watchlist_movie, parent, false)
        return WatchlistViewHolder(view, onWatchedChanged)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WatchlistViewHolder(
        view: View,
        private val onWatchedChanged: (UiMovie, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val title = view.findViewById<TextView>(R.id.movieTitle)
        private val info = view.findViewById<TextView>(R.id.movieInfo)
        private val poster = view.findViewById<ImageView>(R.id.moviePoster)
        private val watchedCheckBox = view.findViewById<CheckBox>(R.id.checkWatched)

        fun bind(movie: UiMovie) {
            title.text = movie.title
            info.text = "${movie.year} • ${movie.type}"

            ImageDecoder.decodeInto(poster, movie.poster)

            watchedCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.isChecked = movie.watched
            watchedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onWatchedChanged(movie, isChecked)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UiMovie>() {
        override fun areItemsTheSame(oldItem: UiMovie, newItem: UiMovie) =
            oldItem.imdbId == newItem.imdbId

        override fun areContentsTheSame(oldItem: UiMovie, newItem: UiMovie) =
            oldItem == newItem
    }
}
