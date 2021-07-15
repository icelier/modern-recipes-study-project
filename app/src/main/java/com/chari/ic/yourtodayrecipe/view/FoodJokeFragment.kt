package com.chari.ic.yourtodayrecipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chari.ic.yourtodayrecipe.R

/**
 * A simple [Fragment] subclass.
 * Use the [FoodJokeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodJokeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_joke, container, false)

        return view
    }

}