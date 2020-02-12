package com.webster.commerces.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.webster.commerces.entity.Category
import kotlinx.android.synthetic.main.category_item_adapter.view.*

class CategoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(category: Category, clickListener: (Category) -> Unit) {
        itemView.labelName.text = category.name
        Picasso.get().load(category.categoryImage).into(itemView.imageCategory)
        itemView.setOnClickListener { clickListener(category) }
    }
}
