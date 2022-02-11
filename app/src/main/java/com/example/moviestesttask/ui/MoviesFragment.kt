package com.example.moviestesttask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestesttask.R
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.databinding.FragmentMoviesBinding
import com.example.moviestesttask.domain.entity.MovieListItem
import com.example.moviestesttask.presentation.MovieEvent
import com.example.moviestesttask.presentation.MoviesViewModel
import com.example.moviestesttask.ui.adapter.GenreAdapter
import com.example.moviestesttask.ui.adapter.MoviesAdapter
import com.example.moviestesttask.ui.adapter.ViewTypes.FILM_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.ViewTypes.GENRE_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.ViewTypes.HEADER_VIEW_TYPE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalCoroutinesApi
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesAdapter = MoviesAdapter()
        val genreAdapter = GenreAdapter(onClick = {
            moviesViewModel.filterMovies(it.title)
        })
        val concatenated = ConcatAdapter(genreAdapter, moviesAdapter)

        val mLayoutManager = GridLayoutManager(context, 2)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (concatenated.getItemViewType(position)) {
                    HEADER_VIEW_TYPE -> 2
                    FILM_VIEW_TYPE -> 1
                    GENRE_VIEW_TYPE -> 2
                    else -> 1
                }
            }
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = concatenated
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesViewModel.filteredMovies.collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> TODO()
                        is Resource.Loading -> {
                            binding.recyclerView.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                        is Resource.Success -> {
                            binding.recyclerView.isVisible = true
                            binding.progressBar.isVisible = false
                            moviesAdapter.films = resource.data?.map {
                                MovieListItem.Film(
                                    it.id,
                                    it.localized_name,
                                    it.name,
                                    it.year,
                                    it.rating,
                                    it.image_url,
                                    it.description,
                                    it.genres
                                )
                            }!!
                        }
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                moviesViewModel.movies.collectLatest { event ->
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
                            genreAdapter.genres = event.genres
                            moviesAdapter.films = event.movies
                        }
                        else -> {}
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