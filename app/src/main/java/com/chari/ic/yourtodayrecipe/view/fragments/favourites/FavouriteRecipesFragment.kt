package com.chari.ic.yourtodayrecipe.view.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.FavouriteRecipesAdapter
import com.chari.ic.yourtodayrecipe.databinding.FragmentFavouriteRecipesBinding
import com.chari.ic.yourtodayrecipe.databinding.FragmentRecipesBinding
import com.chari.ic.yourtodayrecipe.view.fragments.recipes.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private val favouriteRecipesAdapter by lazy { FavouriteRecipesAdapter() }

    private val recipeViewModel: RecipeViewModel by viewModels()

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = recipeViewModel
        binding.adapter = favouriteRecipesAdapter

            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter() {
        binding.favouritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favouritesRecyclerView.adapter = favouriteRecipesAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}