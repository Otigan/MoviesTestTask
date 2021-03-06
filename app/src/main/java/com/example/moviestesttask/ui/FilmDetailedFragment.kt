package com.example.moviestesttask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviestesttask.R
import com.example.moviestesttask.databinding.FragmentFilmDetailedBinding

class FilmDetailedFragment : Fragment(R.layout.fragment_film_detailed) {

    private var _binding: FragmentFilmDetailedBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<FilmDetailedFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmDetailedBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film = args.film

        binding.apply {
            context?.let {
                Glide.with(it)
                    .load(film.image_url)
                    .error(R.drawable.not_found)
                    .into(filmImage)
            }
            txtLocalizedName.text = film.localized_name
            txtOriginalName.text = film.name
            txtYear.text = getString(R.string.film_year, film.year)
            film.rating?.let {
                txtRating.text = getString(R.string.film_rating, it)
            } ?: run {
                txtRating.text =
                    getString(R.string.film_rating_not_found, R.string.film_rating_not_found)
            }
            txtDescription.text = film.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}