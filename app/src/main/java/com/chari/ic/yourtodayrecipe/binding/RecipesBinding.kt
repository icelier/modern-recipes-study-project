package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
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

        when (view) {
            is ImageView -> {
                view.isVisible = responseState is NetworkResult.Error && cachedState.isNullOrEmpty()
            }
            is TextView -> {
                view.isVisible = responseState is NetworkResult.Error && cachedState.isNullOrEmpty()
                responseState?.message?.let { view.text = it }
            }
        }

    }
}