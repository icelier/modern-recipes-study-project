package com.chari.ic.yourtodayrecipe.view.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chari.ic.yourtodayrecipe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecipeBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recipe_bottom_sheet, container, false)
    }
}