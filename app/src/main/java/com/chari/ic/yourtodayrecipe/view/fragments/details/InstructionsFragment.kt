package com.chari.ic.yourtodayrecipe.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.chari.ic.yourtodayrecipe.R
import com.chari.ic.yourtodayrecipe.model.Recipe
import com.chari.ic.yourtodayrecipe.util.Constants

class InstructionsFragment: Fragment() {
    private var bundle: Bundle? = null
    private var currentRecipe: Recipe? = null

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle = arguments
        currentRecipe = bundle?.getParcelable(Constants.KEY_RECIPE_BUNDLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)
        webView = view.findViewById(R.id.webview)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = object: WebViewClient() {}
        currentRecipe?.let { webView.loadUrl(it.sourceUrl) }
    }
}