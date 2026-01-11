package com.example.moviewatchlist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.ui.model.UiMovie

class MovieAdapter(private val onAddToWatchlist: (UiMovie) -> Unit)
    : ListAdapter<UiMovie, MovieAdapter.MovieViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onAddToWatchlist)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(view: View, private val onAddToWatchlist: (UiMovie) -> Unit)
        : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.movieTitle)
        private val info = view.findViewById<TextView>(R.id.movieInfo)
        private val poster = view.findViewById<ImageView>(R.id.moviePoster)
        private val addButton = view.findViewById<ImageButton>(R.id.buttonAddWatchlist)

        fun bind(movie: UiMovie) {
            title.text = movie.title
            info.text = "${movie.year} • ${movie.type}"

            ImageDecoder.decodeInto(poster, movie.poster)

            addButton.setOnClickListener {
                onAddToWatchlist(movie)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UiMovie>() {
        override fun areItemsTheSame(old: UiMovie, new: UiMovie) =
            old.imdbId == new.imdbId

        override fun areContentsTheSame(old: UiMovie, new: UiMovie) =
            old == new
    }
}
