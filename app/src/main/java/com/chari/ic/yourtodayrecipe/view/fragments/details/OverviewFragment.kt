package com.chari.ic.yourtodayrecipe.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chari.ic.yourtodayrecipe.databinding.FragmentOverviewBinding
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.KEY_RECIPE_BUNDLE

class OverviewFragment: Fragment() {
    private var bundle: Bundle? = null
    private var currentRecipe: Recipe? = null

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeImage: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var summaryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle = arguments
        currentRecipe = bundle?.getParcelable(KEY_RECIPE_BUNDLE) 
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.recipe = currentRecipe

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}