package com.chari.ic.yourtodayrecipe.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.chari.ic.yourtodayrecipe.R
import org.jsoup.Jsoup

object RecipeDetailsBinding {
    @BindingAdapter("selected")
    @JvmStatic
    fun getSelected(view: View, selected: Boolean) {
        if (selected) {
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