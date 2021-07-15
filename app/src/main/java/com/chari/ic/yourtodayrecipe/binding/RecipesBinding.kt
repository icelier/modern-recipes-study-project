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
    fun setErrorImageVisibility(
        imageView: ImageView,
        responseState: NetworkResult<RecipeResponse>?,
        cachedState: List<RecipeEntity>?
        ) {
        if (responseState is NetworkResult.Error && cachedState.isNullOrEmpty()) {
            imageView.visibility = View.VISIBLE
        }
        else {
            imageView.visibility = View.INVISIBLE
        }
    }

    @BindingAdapter("apiResponse", "cachedState", requireAll = true)
    @JvmStatic
    fun setErrorTextVisibility(
        textView: TextView,
        responseState: NetworkResult<RecipeResponse>?,
        cachedState: List<RecipeEntity>?
    ) {
        if (responseState is NetworkResult.Error && cachedState.isNullOrEmpty()) {
            textView.visibility = View.VISIBLE
            textView.text = responseState.message
        }
        else {
            textView.visibility = View.INVISIBLE
        }
    }
}