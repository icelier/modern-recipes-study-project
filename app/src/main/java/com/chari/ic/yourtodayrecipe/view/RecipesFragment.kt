package com.chari.ic.yourtodayrecipe.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.RecipeViewModel
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.RecipeAdapter
import com.chari.ic.yourtodayrecipe.databinding.FragmentRecipesBinding
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.API_KEY
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import com.chari.ic.yourtodayrecipe.util.observeOnce
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "RecipesFragment"
@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var shimmerFrame: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView
    private val recipeAdapter by lazy { RecipeAdapter() }
    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.recipeViewModel = recipeViewModel
//        recyclerView = mView.findViewById(R.id.recipe_recycler_view)
//        shimmerFrame = mView.findViewById(R.id.shimmer_frame_layout)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recipeRecyclerView
        shimmerFrame = binding.shimmerFrameLayout

        setupAdapter()
        loadCachedData()
    }

    private fun loadCachedData() {
        Log.d(TAG, "Cached data from database requested")
        lifecycleScope.launch {
            recipeViewModel.cachedRecipes.observeOnce(viewLifecycleOwner) {
                    cachedRecipes ->
                if (cachedRecipes.isNotEmpty()) {
                    stopShimmerFX()
                    recipeAdapter.setData(cachedRecipes[0].recipe)
                } else {
                    fetchRecipeNewData()
                }
            }
        }
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recipeAdapter
        showShimmerFX()
    }

    private fun fetchRecipeNewData() {
        Log.d(TAG, "New data from network requested")
        recipeViewModel.getRecipes(setupQuery())
        recipeViewModel.recipeSearchResult.observe(viewLifecycleOwner) {
            result ->
            when(result) {
                is NetworkResult.Success -> {
                    stopShimmerFX()
                    result.data?.let { recipeAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    stopShimmerFX()
                    tryLoadCachedDataIfNoNetworkConnection()
                    Toast.makeText(
                        requireContext(),
                        result.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading ->
                    showShimmerFX()
            }
        }
    }

    private fun tryLoadCachedDataIfNoNetworkConnection() {
        Log.d(TAG, "No connection. Cached data from database requested")
        lifecycleScope.launch {
            recipeViewModel.cachedRecipes.observe(viewLifecycleOwner) {
                    cachedRecipes ->
                if (cachedRecipes.isNotEmpty()) {
                    recipeAdapter.setData(cachedRecipes[0].recipe)
                }
            }
        }
    }

    private fun setupQuery(): HashMap<String, String> {
        val queries = HashMap<String, String>()

        queries["query"] = "salad"
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
    }

    private fun showShimmerFX() {
        shimmerFrame.startShimmer();
        shimmerFrame.visibility = View.VISIBLE
//        shimmerFrame.showShimmer(true)
    }
    private fun stopShimmerFX() {
        shimmerFrame.stopShimmer();
//        shimmerFrame.hideShimmer()
        shimmerFrame.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}