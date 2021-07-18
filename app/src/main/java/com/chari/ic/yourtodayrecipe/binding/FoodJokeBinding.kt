package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.FoodJokeEntity
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import com.google.android.material.card.MaterialCardView

object FoodJokeBinding {
    @BindingAdapter("jokeResponse", "cachedJoke", requireAll = false)
    @JvmStatic
    fun setCardAndProgressVisibility(
        view: View,
        apiResponse: NetworkResult<FoodJoke>?,
        cachedFoodJokes: List<FoodJokeEntity>?
    ) {
        if (apiResponse == null) {
            if (view is TextView || view is ImageView) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
            return
        }
        when (apiResponse) {
           is NetworkResult.Loading -> {
               when (view) {
                   is ProgressBar -> view.visibility = View.VISIBLE
                   else -> view.visibility = View.INVISIBLE
               }
           }
            is NetworkResult.Error -> {
                when (view) {
                    is ProgressBar -> view.visibility = View.INVISIBLE
                    is MaterialCardView ->
                        view.visibility = if (cachedFoodJokes.isNullOrEmpty()) {
                            View.INVISIBLE
                        } else {
                            View.VISIBLE
                        }
                    is TextView -> {
                        if (!cachedFoodJokes.isNullOrEmpty()) {
                            view.visibility = View.INVISIBLE
                        } else {
                            view.visibility = View.VISIBLE
                            apiResponse.message?.let { view.text = it }
                        }
                    }
                    is ImageView -> {
                        if (!cachedFoodJokes.isNullOrEmpty()) {
                            view.visibility = View.INVISIBLE
                        } else {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
            is NetworkResult.Success -> {
                when (view) {
                    is MaterialCardView -> view.visibility = View.VISIBLE
                    else -> view.visibility = View.INVISIBLE
                }
            }
        }
    }
}