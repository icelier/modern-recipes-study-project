package com.chari.ic.yourtodayrecipe.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.view.fragments.recipes.RecipesFragmentDirections

private const val TAG = "RecipesRowBinding"
object RecipesRowBinding {

        @BindingAdapter("onRecipeSelected")
        @JvmStatic
        fun getRecipeDetails(recipeItemView: ConstraintLayout, recipe: Recipe) {
            recipeItemView.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToRecipeDetailsActivity(recipe)
                    recipeItemView.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String) {
            imageView.load(url) {
                crossfade(600)
                error(R.drawable.ic_error_sad)
            }
        }

        @BindingAdapter("likes")
        @JvmStatic
        fun setLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("time")
        @JvmStatic
        fun setCookingTime(textView: TextView, cookingTimeInMinutes: Int) {
            textView.text = cookingTimeInMinutes.toString()
        }

        @BindingAdapter("vegan")
        @JvmStatic
        fun setVegan(view: View, isVegan: Boolean) {
            if(isVegan) {
                when(view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }
}