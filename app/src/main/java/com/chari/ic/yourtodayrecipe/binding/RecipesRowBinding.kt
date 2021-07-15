package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.chari.ic.yourtodayrecipe.R

object RecipesRowBinding {
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