package com.chari.ic.yourtodayrecipe.view.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.util.Constants
import com.chari.ic.yourtodayrecipe.view.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

private const val TAG = "RecipeBottomSheet"
class RecipeBottomSheet: BottomSheetDialogFragment() {
    private lateinit var recipeViewModel: RecipeViewModel

    private var selectedMealType: String = Constants.DEFAULT_MEAL_TYPE
    private var selectedMealTypeId: Int = 0
    private var selectedDietType: String = Constants.DEFAULT_DIET_TYPE
    private var selectedDietTypeId: Int = 0

    private lateinit var mealChipGroup: ChipGroup
    private lateinit var dietChipGroup: ChipGroup
    private lateinit var applyBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.recipe_bottom_sheet, container, false)
        mealChipGroup = view.findViewById(R.id.meal_type_chip_group)
        dietChipGroup = view.findViewById(R.id.diet_type_chip_group)
        applyBtn = view.findViewById(R.id.apply_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeViewModel.storedMealAndDietTypes.asLiveData().observe(viewLifecycleOwner) {
            mealAndDietTypes ->
            selectedMealType = mealAndDietTypes.selectedMealType
            selectedDietType = mealAndDietTypes.selectedDietType

            updateChipSelected(mealAndDietTypes.selectedMealTypeId, mealChipGroup)
            updateChipSelected(mealAndDietTypes.selectedDietTypeId, dietChipGroup)
        }

        mealChipGroup.setOnCheckedChangeListener { group, checkedChipId ->
            Log.d(TAG, "MealGroup: checked id = $checkedChipId group = $group")
            val chip = group.findViewById<Chip>(checkedChipId)
            Log.d(TAG, "chip = null - ${chip == null}")
            selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            selectedMealTypeId = checkedChipId
        }

        dietChipGroup.setOnCheckedChangeListener { group, checkedChipId ->
            Log.d(TAG, "DietGroup: checked id = $checkedChipId group = $group")
            val chip = group.findViewById<Chip>(checkedChipId)
            Log.d(TAG, "chip = null - ${chip == null}")
            selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            selectedDietTypeId = checkedChipId
        }

        applyBtn.setOnClickListener {
            Log.d(TAG, "meal and diet updated in UI")
            recipeViewModel.saveMealAndDietTypes(
                selectedMealType, selectedMealTypeId,
                selectedDietType, selectedDietTypeId
            )
            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }
    }

    private fun updateChipSelected(chipId: Int, chipGroup: ChipGroup) {
        Log.d(TAG, "in updateChipSelected of BottomSheet")
        if (chipId != 0) {
            Log.d(TAG, "in updateChipSelected of BottomSheet: chipId != 0 -> checking chip!")
            chipGroup.check(chipId)
        }
    }
}