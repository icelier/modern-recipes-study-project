package com.chari.ic.yourtodayrecipe.view

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.adapter.RecipeDetailsPagerAdapter
import com.chari.ic.yourtodayrecipe.util.Constants.Companion.KEY_RECIPE_BUNDLE
import com.chari.ic.yourtodayrecipe.view.fragments.details.IngredientsFragment
import com.chari.ic.yourtodayrecipe.view.fragments.details.InstructionsFragment
import com.chari.ic.yourtodayrecipe.view.fragments.details.OverviewFragment

class RecipeDetailsActivity: AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var pager: ViewPager
    private lateinit var tabLayout: TabLayout

    private val args by navArgs<RecipeDetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pager = findViewById(R.id.view_pager)

        val fragments = arrayListOf<Fragment>(
            OverviewFragment(),
            IngredientsFragment(),
            InstructionsFragment()
        )

        val titles = arrayListOf<String>(
            "Overview",
            "Ingredients",
            "Instructions"
        )
        val resultBundle = Bundle()
        resultBundle.putParcelable(KEY_RECIPE_BUNDLE, args.recipe)

        val pagerAdapter = RecipeDetailsPagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )
        pager.adapter = pagerAdapter

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(pager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}