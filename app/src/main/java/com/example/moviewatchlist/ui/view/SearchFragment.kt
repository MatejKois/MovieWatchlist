package com.example.moviewatchlist.ui.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.moviewatchlist.R
import com.example.moviewatchlist.R.layout.fragment_search
import com.example.moviewatchlist.ui.adapters.MovieAdapter
import com.example.moviewatchlist.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)

        adapter = MovieAdapter { movie -> viewModel.addToWatchlist(movie) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        var searchJob: Job? = null

        searchEditText.addTextChangedListener { text ->
            val query = text.toString()
            searchJob?.cancel()

            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(300)
                val result = viewModel.search(query)
                adapter.submitList(result)
            }
        }
    }
}
