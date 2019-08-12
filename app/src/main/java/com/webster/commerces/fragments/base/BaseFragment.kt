package com.webster.commerces.fragments.base

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.webster.commerces.R

open class BaseFragment : Fragment() {

    val viewGroup by lazy {
        view as ViewGroup
    }

    private val loadingView by lazy {
        val loadingView = layoutInflater.inflate(R.layout.loading_view, viewGroup, false) as View
        loadingView.setOnTouchListener { _, _ -> true }
        loadingView
    }

    fun showLoading() {
        viewGroup.addView(loadingView)
    }

    fun dismissLoading() {
        viewGroup.removeView(loadingView)
    }
}