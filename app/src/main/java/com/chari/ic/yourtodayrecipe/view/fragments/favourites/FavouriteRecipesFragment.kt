package com.chari.ic.yourtodayrecipe.view.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.FavouriteRecipesAdapter
import com.chari.ic.yourtodayrecipe.databinding.FragmentFavouriteRecipesBinding
import com.chari.ic.yourtodayrecipe.view.fragments.recipes.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private val favouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity(), recipeViewModel) }

    private val recipeViewModel: RecipeViewModel by viewModels()

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = recipeViewModel
        binding.adapter = favouriteRecipesAdapter

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_favourite_recipes_menu) {
            recipeViewModel.deleteAllFavouriteRecipes()
            showSnackBar("All recipes removed from Favourites")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK") {}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        favouriteRecipesAdapter.clearContextualActionMode()
    }
}