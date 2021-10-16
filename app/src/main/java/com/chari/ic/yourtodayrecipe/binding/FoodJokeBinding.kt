package com.chari.ic.yourtodayrecipe.binding

import android.view.View
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
        when (apiResponse) {
            is NetworkResult.Loading -> {
                when (view) {
                    is ProgressBar -> view.visibility = View.VISIBLE
                    is MaterialCardView -> view.visibility = View.INVISIBLE
                }
            }
            is NetworkResult.Error -> {
                when (view) {
                    is ProgressBar -> view.visibility = View.INVISIBLE
                    is MaterialCardView -> {
                        view.visibility = View.VISIBLE
                        if (cachedFoodJokes != null) {
                            if (cachedFoodJokes.isEmpty()) {
                                view.visibility = View.INVISIBLE
                            }
                        }
                    }

                }
            }
            is NetworkResult.Success -> {
                when (view) {
                    is ProgressBar -> view.visibility = View.INVISIBLE
                    is MaterialCardView -> view.visibility = View.VISIBLE
                }
            }
        }
    }

    @BindingAdapter("jokeResponseError", "cachedJokeError", requireAll = true)
    @JvmStatic
    fun setErrorViewVisibility(
        view: View,
        apiResponse: NetworkResult<FoodJoke>?,
        cachedFoodJokes: List<FoodJokeEntity>?
    ) {
        if (cachedFoodJokes != null) {
            if (cachedFoodJokes.isEmpty()) {
                view.visibility = View.VISIBLE
                if (view is TextView) {
                    if (apiResponse != null) {
                        view.text = apiResponse.message
                    }
                }
            }
        }
        if (apiResponse is NetworkResult.Success) {
            view.visibility = View.INVISIBLE
        }
    }
}