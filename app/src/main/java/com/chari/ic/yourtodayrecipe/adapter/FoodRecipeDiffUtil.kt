package com.chari.ic.yourtodayrecipe.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chari.ic.yourtodayrecipe.model.Recipe

class FoodRecipeDiffUtil(
    private val oldList: List<Recipe>,
    private val newList: List<Recipe>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldListSize
    }

    override fun getNewListSize(): Int {
        return newListSize
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}