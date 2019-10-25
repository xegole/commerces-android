package com.webster.commerces.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.webster.commerces.R

abstract class BaseFragment : Fragment() {

    private val viewGroup by lazy {
        view as ViewGroup
    }

    abstract fun resourceLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resourceLayout(), container, false)
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