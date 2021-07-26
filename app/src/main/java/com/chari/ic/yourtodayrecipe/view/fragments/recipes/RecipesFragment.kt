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
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "RecipesFragment"
@AndroidEntryPoint
class RecipesFragment : Fragment(),
    SearchView.OnQueryTextListener
{
    private lateinit var shimmerFrame: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView
    private val recipeAdapter by lazy { RecipeAdapter(RecipeAdapter.DIFF_UTIL_CALLBACK) }
    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    private var databaseLoadJob: Job? = null
    private var networkLoadJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
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
        databaseLoadJob?.cancel()

        databaseLoadJob = viewLifecycleOwner.lifecycleScope.launch {
            Log.d(TAG, "CachedRecipes observers: ${recipeViewModel.cachedRecipes.hasObservers()}")
            if (recipeViewModel.cachedRecipes.hasObservers()) {
                recipeViewModel.cachedRecipes.removeObservers(viewLifecycleOwner)
            }
            recipeViewModel.cachedRecipes.observe(viewLifecycleOwner) {
                    cachedRecipes ->
                if (cachedRecipes.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d(TAG, "Cached data from DB received")
                    stopShimmerFX()
                    Log.d(TAG, "${cachedRecipes[0].recipe.results}")
                    recipeAdapter.submitList(cachedRecipes[0].recipe.results)
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
        networkLoadJob?.cancel()

        networkLoadJob = recipeViewModel.getRecipes(recipeViewModel.setupQuery())
        if (recipeViewModel.recipesResult.hasObservers()) {
            recipeViewModel.recipesResult.removeObservers(viewLifecycleOwner)
        }
//        recipeViewModel.recipesResult.removeObservers(viewLifecycleOwner)
        recipeViewModel.recipesResult.observe(viewLifecycleOwner) {
            result ->
            when(result) {
                is NetworkResult.Success -> {
                    stopShimmerFX()
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
        networkLoadJob?.cancel()

        networkLoadJob = recipeViewModel.searchRecipes(recipeViewModel.setupSearchQuery(searchQuery))
        if (recipeViewModel.recipesSearch.hasObservers()) {
            recipeViewModel.recipesSearch.removeObservers(viewLifecycleOwner)
        }
//        recipeViewModel.recipesSearch.removeObservers(viewLifecycleOwner)
        recipeViewModel.recipesSearch.observe(viewLifecycleOwner) {
            result ->
            when(result) {
                is NetworkResult.Success -> {
                    stopShimmerFX()
                    result.data?.let { recipeAdapter.submitList(it.results) }
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
        databaseLoadJob?.cancel()

        databaseLoadJob = lifecycleScope.launch {
            if (recipeViewModel.cachedRecipes.hasObservers()) {
                recipeViewModel.cachedRecipes.removeObservers(viewLifecycleOwner)
            }
//            recipeViewModel.cachedRecipes.removeObservers(viewLifecycleOwner)
            recipeViewModel.cachedRecipes.observe(viewLifecycleOwner) {
                    cachedRecipes ->
                if (cachedRecipes.isNotEmpty()) {
                    recipeAdapter.submitList(cachedRecipes[0].recipe.results)
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
        shimmerFrame.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}