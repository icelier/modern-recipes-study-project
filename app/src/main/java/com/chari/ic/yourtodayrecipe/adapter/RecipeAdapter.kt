package com.chari.ic.yourtodayrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.databinding.RecipesItemLayoutBinding
import com.chari.ic.yourtodayrecipe.model.RecipeDetails
import com.chari.ic.yourtodayrecipe.model.RecipeResponse

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipes = emptyList<RecipeDetails>()

    class RecipeViewHolder(private val binding: RecipesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeDetails) {
          binding.recipe = recipe
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): RecipeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = RecipesItemLayoutBinding.inflate(inflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: RecipeResponse) {
        val diffUtil = FoodRecipeDiffUtil(recipes, newData.results)
        val result = DiffUtil.calculateDiff(diffUtil)
        recipes = newData.results
        result.dispatchUpdatesTo(this)
    }

//    object RecipeDiffUtilL: DiffUtil.ItemCallback<RecipeDetails>() {
//        override fun areItemsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
//            return oldItem == newItem
//        }
//
//    }

}