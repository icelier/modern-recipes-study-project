package com.chari.ic.yourtodayrecipe.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.IngredientsAdapter
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.KEY_RECIPE_BUNDLE

class IngredientsFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val ingredientAdapter by lazy { IngredientsAdapter() }
    private var bundle: Bundle? = null
    private var currentRecipe: Recipe? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle = arguments
        currentRecipe = bundle?.getParcelable(KEY_RECIPE_BUNDLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        recyclerView = view.findViewById(R.id.ingredients_recyclerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ingredientAdapter
        currentRecipe?.extendedIngredients?.let { ingredientAdapter.setData(it) }
    }
}