package com.webster.commerces.adapter.base

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setData(item: T)

    open fun setData(item: T, onClickItemView: ((T) -> Unit)?) {

    }

    fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}