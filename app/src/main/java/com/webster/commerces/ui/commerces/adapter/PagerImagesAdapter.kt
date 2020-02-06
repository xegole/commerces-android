package com.webster.commerces.ui.commerces.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.webster.commerces.ui.commerces.view.ImageFragment
import com.yanzhenjie.album.AlbumFile

class PagerImagesAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var filesList = emptyList<AlbumFile>()

    override fun getItem(position: Int) = ImageFragment.newInstance(filesList[position])

    override fun getCount() = filesList.size

    fun setData(filesList: List<AlbumFile>) {
        this.filesList = filesList
        notifyDataSetChanged()
    }
}