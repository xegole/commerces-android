package com.webster.commerces.ui.commerces.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.webster.commerces.ui.commerces.view.ImageFragment
import com.yanzhenjie.album.AlbumFile

class PagerImagesAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var filesList = ArrayList<AlbumFile>()

    override fun getItem(position: Int) = ImageFragment.newInstance(filesList[position])

    override fun getCount() = filesList.size

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun setData(filesList: List<AlbumFile>) {
        this.filesList.clear()
        this.filesList.addAll(filesList)
        notifyDataSetChanged()
    }
}