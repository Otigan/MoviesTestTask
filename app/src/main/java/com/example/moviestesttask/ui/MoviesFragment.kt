package com.example.moviestesttask.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestesttask.R
import com.example.moviestesttask.databinding.FragmentMoviesBinding
import com.example.moviestesttask.presentation.MovieEvent
import com.example.moviestesttask.presentation.MoviesViewModel
import com.example.moviestesttask.ui.adapter.MoviesAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val moviesViewModel by viewModels<MoviesViewModel>()
    private lateinit var moviesAdapter: MoviesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater)
        return _binding?.root
    }

    private fun setLinearLayoutManager() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = moviesAdapter
    }

    private fun setGridLayoutManager() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = moviesAdapter
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesAdapter = MoviesAdapter(setLinearLayoutManager = {
            setLinearLayoutManager()
        }, setGridLayoutManager = {
            setGridLayoutManager()
        })

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesViewModel.moviesEventFlow.collectLatest { event ->
                    when (event) {
                        is MovieEvent.Error -> {
                            binding.recyclerView.isVisible = false
                            binding.progressBar.isVisible = false
                            Snackbar.make(binding.root, event.errorMessage, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        MovieEvent.Loading -> {
                            binding.recyclerView.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                        is MovieEvent.Success -> {
                            binding.recyclerView.isVisible = true
                            binding.progressBar.isVisible = false
                            moviesAdapter.items = event.movies
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}