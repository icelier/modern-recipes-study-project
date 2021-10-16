package com.chari.ic.yourtodayrecipe.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.databinding.RecipesItemLayoutBinding
import com.chari.ic.yourtodayrecipe.model.Recipe

private const val TAG = "RecipeAdapter"
class RecipeAdapter(diffUtilCallback: DiffUtil.ItemCallback<Recipe>):
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(diffUtilCallback) {
    var recipes = ArrayList<Recipe>()

    class RecipeViewHolder(private val binding: RecipesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
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
        Log.d(TAG, "Binding: ${getItem(position)}")
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }


    object DIFF_UTIL_CALLBACK:
        DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}