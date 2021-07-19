package com.chari.ic.yourtodayrecipe.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.model.Ingredient
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.IMAGE_BASE_URL
import java.util.*

private const val TAG = "IngredientsAdapter"
class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {
    private var ingredients = emptyList<Ingredient>()

    class IngredientsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ingredient_imageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.ingredient_title_textView)
        private val weightTextView: TextView = itemView.findViewById(R.id.ingredient_weight)
        private val unitsTextView: TextView = itemView.findViewById(R.id.ingredient_units)
        private val consistencyTextView: TextView = itemView.findViewById(R.id.ingredient_consistency)
        private val originalTextView: TextView = itemView.findViewById(R.id.ingredient_original)

        fun bind(ingredient: Ingredient) {
            Log.d(TAG, "$IMAGE_BASE_URL${ingredient.image}")
            imageView.load("$IMAGE_BASE_URL${ingredient.image}") {
                crossfade(600)
                error(R.drawable.ic_error_sad)
            }
            titleTextView.text = ingredient.name.replaceFirstChar { char -> char.uppercase(Locale.ROOT) }
            weightTextView.text = "%.2f".format(ingredient.amount)
            unitsTextView.text = ingredient.unit
            consistencyTextView.text = ingredient.consistency
            originalTextView.text = ingredient.original
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_item_layout, parent, false)
        return IngredientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newIngredients: List<Ingredient>) {
        val diffUtil = FoodRecipeDiffUtil(ingredients, newIngredients)
        val result = DiffUtil.calculateDiff(diffUtil)
        ingredients = newIngredients
        result.dispatchUpdatesTo(this)
    }

}