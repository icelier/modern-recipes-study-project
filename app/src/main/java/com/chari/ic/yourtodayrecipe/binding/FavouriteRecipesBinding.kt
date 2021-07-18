package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.adapter.FavouriteRecipesAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity

object FavouriteRecipesBinding {

    @BindingAdapter("viewVisibility", "setData", requireAll = false)
    @JvmStatic
    fun setDataAndViewVisibility(
        view: View,
        favouriteRecipes: List<FavouritesEntity>?,
        favouriteRecipesAdapter: FavouriteRecipesAdapter?
    ) {
        if (favouriteRecipes.isNullOrEmpty()) {
            when (view) {
                is ImageView -> view.visibility = View.VISIBLE
                is TextView -> view.visibility = View.VISIBLE
                is RecyclerView -> view.visibility = View.INVISIBLE
            }
        } else {
            when (view) {
                is ImageView -> view.visibility = View.INVISIBLE
                is TextView -> view.visibility = View.INVISIBLE
                is RecyclerView -> {
                    view.visibility = View.VISIBLE
                    favouriteRecipesAdapter?.setData(favouriteRecipes)
                }
            }
        }
    }
}