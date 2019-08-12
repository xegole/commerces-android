package com.webster.commerces.extensions

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.webster.commerces.R


fun Fragment.goToActivity(clazz: Class<*>) {
    val intent = Intent(context, clazz)
    startActivity(intent)
}

fun Fragment.openActivityWithBundle(bundle: Bundle, clazz: Class<*>) {
    val intent = Intent(context, clazz)
    intent.putExtras(bundle)
    startActivity(intent)
}

fun Fragment.openActivityWithBundleOptions(view: View, bundle: Bundle, clazz: Class<*>) {
    val intent = Intent(context, clazz)
    intent.putExtras(bundle)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context as AppCompatActivity,
        view,
        getString(R.string.transition_name_commerce)
    )
    startActivity(intent, options.toBundle())
}