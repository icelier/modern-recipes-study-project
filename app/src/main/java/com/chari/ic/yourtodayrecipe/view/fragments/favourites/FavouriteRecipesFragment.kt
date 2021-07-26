package com.chari.ic.yourtodayrecipe.view.fragments.favourites

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.FavouriteRecipesAdapter
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FavouritesFragment"
@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(this).get(RecipeViewModel::class.java)
    }

    private val favouriteRecipesAdapter by lazy {
        FavouriteRecipesAdapter(requireActivity(),
            recipeViewModel
        ) }

//    private var _binding: FragmentFavouriteRecipesBinding? = null
//    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
//        Log.d(TAG, "ViewModel == null: ${recipeViewModel}")
//        Log.d(TAG, "ViewModel cached favourites == null: ${recipeViewModel.cachedFavouriteRecipes}")
//        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewmodel = recipeViewModel
//        binding.adapter = favouriteRecipesAdapter

        val view = inflater.inflate(R.layout.fragment_favourite_recipes, container, false)
        noDataImageView = view.findViewById(R.id.no_data_imageView)
        noDataTextView = view.findViewById(R.id.no_data_textView)
        recyclerView = view.findViewById(R.id.favourites_recycler_view)

        setHasOptionsMenu(true)

        return view
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        recipeViewModel.cachedFavouriteRecipes.observe(viewLifecycleOwner) {
            cachedFavourites ->
            Log.d(TAG, "cached favourites is null: ${cachedFavourites == null} or empty ${cachedFavourites.isEmpty()}")
            if (cachedFavourites.isNotEmpty()) {
                noDataImageView.visibility = View.INVISIBLE
                noDataTextView.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
                favouriteRecipesAdapter.submitList(cachedFavourites)
            } else {
                noDataImageView.visibility = View.VISIBLE
                noDataTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favouriteRecipesAdapter
//        binding.favouritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.favouritesRecyclerView.adapter = favouriteRecipesAdapter
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
//            binding.root,
            recyclerView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //        _binding = null
        favouriteRecipesAdapter.clearContextualActionMode()
    }
}