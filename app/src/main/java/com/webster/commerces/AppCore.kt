package com.webster.commerces

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp
import com.webster.commerces.utils.MediaLoader
import com.webster.commerces.utils.Prefs
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import io.fabric.sdk.android.Fabric
import java.util.*

class AppCore : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        FirebaseApp.initializeApp(this)
        Fabric.with(this, Crashlytics())

        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        )
    }

    companion object {
        lateinit var instance: AppCore
        lateinit var prefs: Prefs
    }
}