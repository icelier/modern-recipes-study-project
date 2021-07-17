package com.chari.ic.yourtodayrecipe.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.databinding.FragmentOverviewBinding
import com.chari.ic.yourtodayrecipe.databinding.FragmentRecipesBinding
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.view.KEY_RECIPE_BUNDLE

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

//        val view = inflater.inflate(R.layout.fragment_overview, container, false)
//        recipeImage = view.findViewById(R.id.main_imageView)
//        titleTextView = view.findViewById(R.id.title_textView)
//        summaryTextView = view.findViewById(R.id.summary_textView)
//
//        recipeImage.load(currentRecipe?.image)
//        titleTextView.text = currentRecipe?.title
//        summaryTextView.text = currentRecipe?.summary

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}