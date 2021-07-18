package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.chari.ic.yourtodayrecipe.util.NetworkResult

object RecipesBinding {
    @BindingAdapter("apiResponse", "cachedState", requireAll = true)
    @JvmStatic
    fun setErrorImageAndTextVisibility(
        view: View,
        responseState: NetworkResult<RecipeResponse>?,
        cachedState: List<RecipeEntity>?
        ) {
            if (responseState == null) {
                view.visibility = View.VISIBLE
            }
            if (responseState is NetworkResult.Error && cachedState.isNullOrEmpty()) {
                view.visibility = View.VISIBLE
                if (view is TextView) {
                    responseState?.message?.let { view.text = it }
                }
            } else {
                view.visibility = View.INVISIBLE
            }

    }
}