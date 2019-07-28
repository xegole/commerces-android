package com.webster.commerces

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class AppCore : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}