package com.chari.ic.yourtodayrecipe.adapter

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.data.database.entities.FavouritesEntity
import com.chari.ic.yourtodayrecipe.databinding.FavouriteItemLayoutBinding
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.view.fragments.favourites.FavouriteRecipesFragmentDirections
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

private const val TAG ="FavouriteRecipesAdapter"
class FavouriteRecipesAdapter(
    private val activity: FragmentActivity,
    private val recipeViewModel: RecipeViewModel
) : ListAdapter<FavouritesEntity, FavouriteRecipesAdapter.FavouriteRecipesHolder>(DIFF_UTIL_CALLBACK),
    ActionMode.Callback
{
    private lateinit var actionMode: ActionMode
    private lateinit var rootView: View

    private var favouriteRecipes = emptyList<FavouritesEntity>()

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavouritesEntity>()

    inner class FavouriteRecipesHolder(private val binding: FavouriteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
    {

        fun bind(favouriteEntity: FavouritesEntity) {
            rootView = binding.root
            /**
             * Single click listener
             */
            binding.root.setOnClickListener {
                if (multiSelection) {
                    selectRecipe(this, favouriteEntity)
                } else {
                    val action = FavouriteRecipesFragmentDirections
                        .actionFavouriteRecipesFragmentToRecipeDetailsActivity(favouriteEntity.favouriteRecipe)
                    binding.root.findNavController().navigate(action)
                }
            }

            /**
             * Long click listener
             */
            binding.root.setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    activity.startActionMode(this@FavouriteRecipesAdapter)

                    selectRecipe(this, favouriteEntity)
                    true
                } else {
                    multiSelection = false
                    false
                }

            }

            binding.favourite = favouriteEntity
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavouriteItemLayoutBinding.inflate(inflater, parent, false)
        return FavouriteRecipesHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteRecipesHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val diffUtil = FoodRecipeDiffUtil(favouriteRecipes, newFavouriteRecipes)
        val result = DiffUtil.calculateDiff(diffUtil)
        favouriteRecipes = newFavouriteRecipes
        result.dispatchUpdatesTo(this)
    }

    private fun handleActionModeByRecipesSelected() {
        when (selectedRecipes.size) {
            0 -> actionMode.finish()
            1 -> actionMode.title = "${selectedRecipes.size} item(s) selected"
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        actionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_favourite_recipe_menu) {
            selectedRecipes.forEach {
                recipeViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} item(s) deleted")
            mode?.finish()
        }

        return true
    }

    private fun selectRecipe(
        holder: FavouriteRecipesAdapter.FavouriteRecipesHolder,
        favouriteEntity: FavouritesEntity
    ) {
        if (selectedRecipes.contains(favouriteEntity)) {
            selectedRecipes.remove(favouriteEntity)
            changeRecipeStyle(
                holder,
                R.color.cardBackgroundLightColor,
                R.color.colorPrimary
            )
            handleActionModeByRecipesSelected()
        } else {
            selectedRecipes.add(favouriteEntity)
            changeRecipeStyle(
                holder,
                R.color.cardBackgroundColor,
                R.color.strokeColor
            )
            handleActionModeByRecipesSelected()
        }
    }

    private fun changeRecipeStyle (
        holder: FavouriteRecipesHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.favourite_recipes_row_layout)
            .setBackgroundColor(
                ContextCompat.getColor(activity, backgroundColor)
            )
        holder.itemView.findViewById<MaterialCardView>(R.id.favourite_recipe_card).strokeColor =
            ContextCompat.getColor(activity, strokeColor)
    }

    private fun applyStatusBarColor(color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, color)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setAction("OK"){}
            .show()
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }

    object DIFF_UTIL_CALLBACK:
        DiffUtil.ItemCallback<FavouritesEntity>() {
        override fun areItemsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
            return oldItem == newItem
        }
    }
}