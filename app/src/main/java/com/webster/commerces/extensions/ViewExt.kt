package com.webster.commerces.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager


fun View.goActivity(clazz: Class<*>, isFinish: Boolean = false) {
    val intent = Intent(context, clazz)
    context.startActivity(intent)
    if (isFinish) {
        (context as Activity).finish()
    }
}

fun View.hideKeyboard() {
    val inputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.expand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (parent as View).width,
        View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    layoutParams.height = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
                else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    a.duration = (targetHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}