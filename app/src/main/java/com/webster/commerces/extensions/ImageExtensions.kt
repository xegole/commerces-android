package com.webster.commerces.extensions

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.lang.Exception


fun ImageView.loadUrl(url: String) {
    Picasso.get().load(url).into(this)
}

inline fun ImageView.loadUrl(url: String, callback: KCallback.() -> Unit) {
    Picasso.get().load(url).intoWithCallback(this, callback)
}

inline fun RequestCreator.intoWithCallback(target: ImageView, callback: KCallback.() -> Unit) {
    this.into(target, KCallback().apply(callback))
}

class KCallback : Callback {

    private var onSuccess: (() -> Unit)? = null
    private var onError: (() -> Unit)? = null

    override fun onSuccess() {
        onSuccess?.invoke()
    }

    override fun onError(e: Exception?) {
        onError?.invoke()
    }

    fun onSuccess(function: () -> Unit) {
        this.onSuccess = function
    }

    fun onError(function: () -> Unit) {
        this.onError = function
    }
}