package com.webster.commerces.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.webster.commerces.R


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val viewGroup by lazy {
        findViewById<View>(android.R.id.content) as ViewGroup
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