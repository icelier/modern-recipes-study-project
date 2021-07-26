package com.chari.ic.yourtodayrecipe.binding

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.chari.ic.yourtodayrecipe.data.database.entities.FoodJokeEntity
import com.chari.ic.yourtodayrecipe.model.FoodJoke
import com.chari.ic.yourtodayrecipe.util.NetworkResult
import com.google.android.material.card.MaterialCardView

private const val TAG = "FoodJokeBinding"
object FoodJokeBinding {
    @BindingAdapter("jokeResponse", "cachedJoke", requireAll = false)
    @JvmStatic
    fun setCardAndProgressVisibility(
        view: View,
        apiResponse: NetworkResult<FoodJoke>?,
        cachedFoodJokes: List<FoodJokeEntity>?
    ) {
        Log.d(
            TAG,
            "jokeResponse == null: ${apiResponse == null} cachedJoke == null: ${cachedFoodJokes == null}"
        )
//        if (cachedFoodJokes.isNullOrEmpty()) {
//            when (view) {
//                is
//            }
//        }
//        if (apiResponse is NetworkResult.Loading) {
//            when (view) {
//                is ProgressBar -> view.visibility = View.VISIBLE
//                else -> view.visibility = View.INVISIBLE
//            }
//        } else if (apiResponse is NetworkResult.Error && cachedFoodJokes.isNullOrEmpty()) {
//            if (view is ImageView) {
//                if (!cachedFoodJokes.isNullOrEmpty()) {
//                    view.visibility = View.INVISIBLE
//                } else {
//                    view.visibility = View.VISIBLE
//                }
//            } else if (view is TextView) {
//                if (!cachedFoodJokes.isNullOrEmpty()) {
//                    view.visibility = View.INVISIBLE
//                } else {
//                    view.visibility = View.VISIBLE
//                    apiResponse.message?.let { view.text = it }
//                }
//            } else if (view is MaterialCardView) {
//                    view.visibility = if (cachedFoodJokes.isNullOrEmpty()) {
//                        View.INVISIBLE
//                    } else {
//                        View.VISIBLE
//                    }
//                } else if (view is ProgressBar) {
//                    view.visibility = View.INVISIBLE
//                }
//            } else if (apiResponse is NetworkResult.Success)  {
//                when (view) {
//                    is MaterialCardView -> view.visibility = View.VISIBLE
//                    else -> view.visibility = View.INVISIBLE
//                }
//            }
//        }


//        if (apiResponse == null) {
//            if (view is TextView || view is ImageView) {
//                view.visibility = View.VISIBLE
//            } else {
//                view.visibility = View.INVISIBLE
//            }
//        }
        when (apiResponse) {
            is NetworkResult.Loading -> {
                when (view) {
                    is ProgressBar -> view.visibility = View.VISIBLE
                    is MaterialCardView -> view.visibility = View.INVISIBLE
                }
            }
            is NetworkResult.Error -> {
                Log.d(TAG, "Network Error: cachedJoke == null: - ${cachedFoodJokes == null}")
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
//                    is TextView -> {
//                        if (!cachedFoodJokes.isNullOrEmpty()) {
//                            view.visibility = View.INVISIBLE
//                        } else {
//                            view.visibility = View.VISIBLE
//                            apiResponse.message?.let { view.text = it }
//                        }
//                    }
//                    is ImageView -> {
//                        if (!cachedFoodJokes.isNullOrEmpty()) {
//                            view.visibility = View.INVISIBLE
//                        } else {
//                            view.visibility = View.VISIBLE
//                        }
//                    }

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