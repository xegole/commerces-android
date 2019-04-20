package com.webster.commerces.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.utils.ConstantsArray
import kotlinx.android.synthetic.main.pager_item.view.*


class ImagePagerAdapter(context: Context, private val imagesBanner: List<String>) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return imagesBanner.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = inflater.inflate(R.layout.pager_item, container, false)
        Picasso.get().load(imagesBanner[position]).into(itemView.imageBanner)
        container.addView(itemView, ConstantsArray.FIRST)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}