package com.webster.commerces

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.webster.commerces.utils.MediaLoader
import com.webster.commerces.utils.Prefs
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import java.util.*

class AppCore : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        FirebaseApp.initializeApp(this)

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