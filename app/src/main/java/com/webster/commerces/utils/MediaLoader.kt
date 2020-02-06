package com.webster.commerces.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader
import java.io.File

class MediaLoader : AlbumLoader {

    override fun load(imageView: ImageView?, albumFile: AlbumFile?) {
        load(imageView, albumFile?.path)
    }

    override fun load(imageView: ImageView?, url: String?) {
        url?.let {
            val image = File(url)
            Picasso.get().load(image).into(imageView)
        }
    }
}