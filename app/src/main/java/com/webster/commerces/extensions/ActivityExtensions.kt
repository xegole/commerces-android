package com.webster.commerces.extensions

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.webster.commerces.R

fun AppCompatActivity.goToActivity(clazz: Class<*>, isFinish: Boolean = true) {
    val intent = Intent(this, clazz)
    startActivity(intent)
    if (isFinish) {
        finish()
    }
}

fun AppCompatActivity.openActivityWithBundle(bundle: Bundle, clazz: Class<*>) {
    val intent = Intent(this, clazz)
    intent.putExtras(bundle)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.addFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.containerFragment, fragment).commit()
}

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
fun AppCompatActivity.openActivityWithBundleOptions(view: View, bundle: Bundle, clazz: Class<*>) {
    val intent = Intent(this, clazz)
    intent.putExtras(bundle)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        this,
        view,
        getString(R.string.transition_name_commerce)
    )
    startActivity(intent, options.toBundle())
}