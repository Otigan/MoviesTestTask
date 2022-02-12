package com.example.moviestesttask.ui

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestesttask.R
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.databinding.FragmentFilmsBinding
import com.example.moviestesttask.domain.entity.FilmListItem
import com.example.moviestesttask.presentation.MoviesViewModel
import com.example.moviestesttask.presentation.util.FilmEvent
import com.example.moviestesttask.ui.adapter.FilmAdapter
import com.example.moviestesttask.ui.adapter.GenreAdapter
import com.example.moviestesttask.ui.adapter.util.ViewTypes.FILM_HEADER_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.util.ViewTypes.FILM_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.util.ViewTypes.GENRE_HEADER_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.util.ViewTypes.GENRE_VIEW_TYPE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FilmsFragment : Fragment(R.layout.fragment_films) {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private val moviesViewModel by viewModels<MoviesViewModel>()
    private lateinit var filmAdapter: FilmAdapter
    private lateinit var genreAdapter: GenreAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedPosition = savedInstanceState?.getInt("selectedPosition")
        filmAdapter = FilmAdapter(onClick = {
            navigateToDetailScreen(it, it.localized_name)
        })
        genreAdapter = GenreAdapter(
            onClick = { genre ->
                moviesViewModel.filterMovies(genre.title)
            }
        )
        selectedPosition?.let {
            genreAdapter.selectedPosition = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmsBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedPosition", genreAdapter.selectedPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val concatenated = ConcatAdapter(genreAdapter, filmAdapter)

        val mLayoutManager = getFooterAdjustedGridLayoutManager(concatenated, requireContext())

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
                            resource.data?.let {
                                filmAdapter.films = it
                            }
                        }
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    moviesViewModel.films.collectLatest { event ->
                        when (event) {
                            is FilmEvent.Error -> {
                                binding.recyclerView.isVisible = false
                                binding.progressBar.isVisible = false
                                Snackbar.make(
                                    binding.root,
                                    event.errorMessage,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            FilmEvent.Loading -> {
                                binding.recyclerView.isVisible = false
                                binding.progressBar.isVisible = true
                            }
                            is FilmEvent.Success -> {
                                binding.recyclerView.isVisible = true
                                binding.progressBar.isVisible = false
                                filmAdapter.films = event.films
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetailScreen(film: FilmListItem.Film, title: String) {
        val action =
            FilmsFragmentDirections.actionMoviesFragmentToFilmDetailedFragment(film, title)
        findNavController().navigate(action)
    }

    private fun getFooterAdjustedGridLayoutManager(
        concatAdapter: ConcatAdapter,
        context: Context
    ): GridLayoutManager {
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (concatAdapter.getItemViewType(position)) {
                    FILM_HEADER_VIEW_TYPE -> 2
                    FILM_VIEW_TYPE -> 1
                    GENRE_HEADER_VIEW_TYPE -> 2
                    GENRE_VIEW_TYPE -> 2
                    else -> 2
                }
            }
        }
        return layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}