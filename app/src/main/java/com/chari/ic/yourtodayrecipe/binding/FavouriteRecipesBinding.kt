package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.adapter.FavouriteRecipesAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity

object FavouriteRecipesBinding {

    @BindingAdapter("setVisibility", "setData", requireAll = false)
    @JvmStatic
    fun setDataAndViewVisibility(
        view: View,
        favouriteRecipes: List<FavouritesEntity>?,
        favouriteRecipesAdapter: FavouriteRecipesAdapter?
    ) {
        when (view) {
            is RecyclerView -> {
                val noData = favouriteRecipes.isNullOrEmpty()
                view.isInvisible = noData
                if (!noData) {
                    favouriteRecipesAdapter?.submitList(favouriteRecipes)
                }
            }
            else -> {
                view.isVisible = favouriteRecipes.isNullOrEmpty()
            }
        }
    }
}