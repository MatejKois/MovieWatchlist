package com.example.moviewatchlist.ui.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.R.layout.fragment_search
import com.example.moviewatchlist.ui.adapters.MovieAdapter
import com.example.moviewatchlist.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment(fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: MovieAdapter
    private lateinit var placeholderContainer: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)
        placeholderContainer = view.findViewById(R.id.placeholderContainer)

        placeholderContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        adapter = MovieAdapter { movie -> viewModel.addToWatchlist(movie) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.liveMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        searchEditText.addTextChangedListener { text ->
            viewModel.search(text.toString())
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)

            if (result.isNullOrEmpty()) {
                placeholderContainer.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                placeholderContainer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }
}
