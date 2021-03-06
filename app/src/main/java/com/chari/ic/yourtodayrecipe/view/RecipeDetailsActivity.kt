package com.chari.ic.yourtodayrecipe.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.RecipeDetailsPagerAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.KEY_RECIPE_BUNDLE
import com.chari.ic.yourtodayrecipe.view.fragments.details.IngredientsFragment
import com.chari.ic.yourtodayrecipe.view.fragments.details.InstructionsFragment
import com.chari.ic.yourtodayrecipe.view.fragments.details.OverviewFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class RecipeDetailsActivity: AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var pager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var detailsLayout: ConstraintLayout

    private val args by navArgs<RecipeDetailsActivityArgs>()

    private val recipeViewModel: RecipeViewModel by lazy {
        ViewModelProvider(this).get(RecipeViewModel::class.java)
    }

    private var recipeSavedToFavourites = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        detailsLayout = findViewById(R.id.details_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pager = findViewById(R.id.view_pager)

        val fragments = arrayListOf(
            OverviewFragment(),
            IngredientsFragment(),
            InstructionsFragment()
        )

        val titles = arrayListOf(
            "Overview",
            "Ingredients",
            "Instructions"
        )
        val resultBundle = Bundle()
        args.let { resultBundle.putParcelable(KEY_RECIPE_BUNDLE, args.recipe) }

        val pagerAdapter = RecipeDetailsPagerAdapter(
            resultBundle,
            fragments,
            this
        )
        pager.adapter = pagerAdapter

        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, pager) {
            tab, position -> tab.text = titles[position]
        }
            .attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_details_menu, menu)
        val saveToFavouriteItem = menu?.findItem(R.id.save_to_favourites)
        if (saveToFavouriteItem != null) {
            checkSavedFavouriteRecipes(saveToFavouriteItem)
        }

        return true
    }

    private fun checkSavedFavouriteRecipes(item: MenuItem) {
        recipeViewModel.cachedFavouriteRecipes.observe(this) {
            savedRecipes ->
            try {
                for (recipe in savedRecipes) {
                    if (recipe.favouriteRecipe.id == args.recipe.id) {
                        savedRecipeId = recipe.id
                        recipeSavedToFavourites = true
                    }
                    markFavouriteRecipe(item)
                }
            } catch (e: Exception) {
                throw Exception("Failed to get recipe id")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites) {
            if (!recipeSavedToFavourites) {
                saveToFavourites()
            } else {
                deleteFromFavourites()
            }
            markFavouriteRecipe(item)
        }

        return true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK") {}
            .show()
    }

    private fun markFavouriteRecipe(item: MenuItem) {
         if (recipeSavedToFavourites) {
            item.icon.setTint(ContextCompat.getColor(this, R.color.yellow))
        } else {
            item.icon.setTint(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun saveToFavourites() {
        val favouritesEntity = FavouritesEntity(
            0,
            args.recipe
        )

        recipeViewModel.insertFavouriteRecipe(favouritesEntity)
        recipeSavedToFavourites = true
        showSnackBar("Recipe saved")
    }

    private fun deleteFromFavourites() {
        val favouritesEntity = FavouritesEntity(
            savedRecipeId,
            args.recipe
        )
        recipeViewModel.deleteFavouriteRecipe(favouritesEntity)
        recipeSavedToFavourites = false
        showSnackBar("Recipe removed from Favourites")
    }
}