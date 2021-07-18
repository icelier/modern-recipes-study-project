package com.chari.ic.yourtodayrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.databinding.FavouriteItemLayoutBinding
import com.chari.ic.yourtodayrecipe.model.Recipe

class FavouriteRecipesAdapter: RecyclerView.Adapter<FavouriteRecipesAdapter.FavouriteRecipesViewHolder>() {
    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class FavouriteRecipesViewHolder(private val binding: FavouriteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteRecipe: FavouritesEntity) {
            binding.favourite = favouriteRecipe
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): FavouriteRecipesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = FavouriteItemLayoutBinding.inflate(inflater, parent, false)
                return FavouriteRecipesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipesViewHolder {
        return FavouriteRecipesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavouriteRecipesViewHolder, position: Int) {
        holder.bind(favouriteRecipes[position])
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val diffUtil = FoodRecipeDiffUtil(favouriteRecipes, newFavouriteRecipes)
        val result = DiffUtil.calculateDiff(diffUtil)
        favouriteRecipes = newFavouriteRecipes
        result.dispatchUpdatesTo(this)
    }
}