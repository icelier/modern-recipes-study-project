package com.chari.ic.yourtodayrecipe.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.data.database.entities.RecipeEntity
import com.chari.ic.yourtodayrecipe.model.RecipeResponse
import com.chari.ic.yourtodayrecipe.util.NetworkResult

private const val TAG = "RecipesBinding"
object RecipesBinding {

    @BindingAdapter("apiResponse", "cachedState", requireAll = true)
    @JvmStatic
    fun setErrorImageAndTextVisibility(
        view: View,
        responseState: NetworkResult<RecipeResponse>?,
        cachedState: List<RecipeEntity>?
        ) {
            Log.d(TAG, "networkResult == null: ${responseState == null}")
            Log.d(TAG, "cachedRecipes == null: ${cachedState == null}")
//            if (responseState == null) {
//                view.visibility = View.INVISIBLE
//                return
//            }
            if (responseState is NetworkResult.Error && cachedState.isNullOrEmpty()) {
                if (view is ImageView || view is TextView) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.INVISIBLE
                }
                if (view is TextView) {
                    responseState.message?.let { view.text = it }
                }
            } else {
                if (view is RecyclerView) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.INVISIBLE
                }
            }

    }
}