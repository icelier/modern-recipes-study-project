package com.chari.ic.yourtodayrecipe.view.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.databinding.FragmentFoodJokeBinding
import com.chari.ic.yourtodayrecipe.util.Constants
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {
    private lateinit var recipeViewModel: RecipeViewModel

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var currentJoke = "No current Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        binding.viewmodel = recipeViewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeViewModel.getFoodJoke(Constants.API_KEY)
        recipeViewModel.foodJoke.observe(viewLifecycleOwner) {
            foodJoke ->
            when(foodJoke) {
                is NetworkResult.Success -> {
                    if (foodJoke.data != null) {
                        binding.foodJokeTextView.text = foodJoke.data.joke
                        currentJoke = foodJoke.data.joke
                    }
                }
                is NetworkResult.Error -> {
                    loadCachedFoodJoke()
                    Toast.makeText(
                        requireContext(),
                        foodJoke.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun loadCachedFoodJoke() {
        viewLifecycleOwner.lifecycleScope.launch {
            recipeViewModel.cachedFoodJokes.observe(viewLifecycleOwner) {
                    foodJokes ->
                if (!foodJokes.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = foodJokes[0].foodJoke.joke
                    currentJoke = foodJokes[0].foodJoke.joke
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentJoke)
                type = Constants.SHARE_TYPE
            }
            startActivity(shareIntent)
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}