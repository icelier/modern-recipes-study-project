package com.chari.ic.yourtodayrecipe.view.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.RecipeAdapter
import com.chari.ic.yourtodayrecipe.databinding.FragmentRecipesBinding
import com.chari.ic.yourtodayrecipe.util.NetworkListener
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import com.chari.ic.yourtodayrecipe.util.observeOnce
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "RecipesFragment"
@AndroidEntryPoint
class RecipesFragment : Fragment(),
    SearchView.OnQueryTextListener
{
    private lateinit var shimmerFrame: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView
    private val recipeAdapter by lazy { RecipeAdapter() }
    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = recipeViewModel

        recyclerView = binding.recipeRecyclerView
        shimmerFrame = binding.shimmerFrameLayout

         setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipesFab.setOnClickListener {
            if (recipeViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheet)
            } else {
                recipeViewModel.showNetworkStatus()
            }

        }

        setupAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d(TAG, "Network available: $status")
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    loadCachedData()

                }
        }

        recipeViewModel.storedBackOnline.asLiveData().observe(viewLifecycleOwner) {
            Log.d(TAG, " BackOnline liveData got updated: backOnline became $it")
            recipeViewModel.backOnline = it
        }

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    private fun loadCachedData() {
        Log.d(TAG, "Cached data from database requested")
        viewLifecycleOwner.lifecycleScope.launch {
            recipeViewModel.cachedRecipes.observeOnce(viewLifecycleOwner) {
                    cachedRecipes ->
                if (cachedRecipes.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d(TAG, "Cached data from DB received")
                    stopShimmerFX()
                    recipeAdapter.setData(cachedRecipes[0].recipe)
                } else {
                    Log.d(TAG, "No cached data from DB")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@RecipesFragment)
        }
    }

    override fun onQueryTextSubmit(searchQuery: String?): Boolean {
        if (!searchQuery.isNullOrBlank()) {
            searchRecipes(searchQuery)
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun fetchRecipeNewData() {
        Log.d(TAG, "New data from network required")
        recipeViewModel.getRecipes(recipeViewModel.setupQuery())
        recipeViewModel.recipesResult.observe(viewLifecycleOwner) {
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

    private fun searchRecipes(searchQuery: String) {
        showShimmerFX()
        recipeViewModel.searchRecipes(recipeViewModel.setupSearchQuery(searchQuery))
        recipeViewModel.recipesSearchResult.observe(viewLifecycleOwner) {
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

    private fun showShimmerFX() {
        shimmerFrame.startShimmer();
        shimmerFrame.visibility = View.VISIBLE
    }
    private fun stopShimmerFX() {
        shimmerFrame.stopShimmer();
        shimmerFrame.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}