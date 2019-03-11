package com.webster.commerces.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.CategoryVH
import com.webster.commerces.entity.Category


class CategoryAdapter(
    private val items: ArrayList<Category>,
    private val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item_adapter, parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: CategoryVH, position: Int) {
        viewHolder.setData(items[position], clickListener)
    }

    fun addItemList(list: List<Category>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}