package com.chari.ic.yourtodayrecipe.view.fragments.recipes

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
    private var fromBottomSheet = false

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    private var networkLoadJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = recipeViewModel
        fromBottomSheet = args.backFromBottomSheet

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
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    loadCachedData()
                }
        }

        recipeViewModel.storedBackOnline.asLiveData().observe(viewLifecycleOwner) {
            recipeViewModel.backOnline = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

        // unregister network callback to not listen for network changes
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkListener)
        }
    }

    override fun onDestroy() {
        networkLoadJob?.cancel()

        super.onDestroy()
    }

    private fun loadCachedData() {
        recipeViewModel.cachedRecipes.observe(viewLifecycleOwner) {
                cachedRecipes ->
            if (cachedRecipes.isNotEmpty() && !fromBottomSheet) {
                stopShimmerFX()
                recipeAdapter.submitList(cachedRecipes[0].recipe.results)
            } else {
                fromBottomSheet = false
                fetchRecipeNewData()
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
        networkLoadJob?.cancel()

        networkLoadJob = recipeViewModel.getRecipes(recipeViewModel.setupQuery())
        if (recipeViewModel.recipesResult.hasObservers()) {
            recipeViewModel.recipesResult.removeObservers(viewLifecycleOwner)
        }

        recipeViewModel.recipesResult.observe(viewLifecycleOwner) {
            result ->
            when(result) {
                is NetworkResult.Success -> {
                    stopShimmerFX()
                    recipeViewModel.saveMealAndDietTypes()
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

        recipeViewModel.recipesSearch.observe(viewLifecycleOwner) {
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

    private fun tryLoadCachedDataIfNoNetworkConnection() {

        recipeViewModel.cachedRecipes.observeOnce(viewLifecycleOwner) {
                cachedRecipes ->
            if (cachedRecipes.isNotEmpty()) {
                recipeAdapter.submitList(cachedRecipes[0].recipe.results)
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

}