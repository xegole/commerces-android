package com.webster.commerces.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var enableSwipe: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (enableSwipe) {
            super.onTouchEvent(event)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (enableSwipe) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }

    fun setEnableSwipe(swipe: Boolean) {
        enableSwipe = swipe
    }
}