package com.webster.commerces.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore


object Utils {

    fun getPath(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val result: String?
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor == null) {
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result ?: Constants.EMPTY_STRING
    }


    fun getPathFromURI(contentUri: Uri, context: Context): String {
        var res: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(columnIndex)
        }
        cursor?.close()
        return res ?: Constants.EMPTY_STRING
    }
}