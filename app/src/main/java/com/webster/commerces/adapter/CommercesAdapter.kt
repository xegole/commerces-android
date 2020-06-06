package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.CommerceVH
import com.webster.commerces.entity.Commerce

class CommercesAdapter(
    private val items: ArrayList<Commerce> = arrayListOf(),
    private val clickListener: (Commerce, View) -> Unit
) : RecyclerView.Adapter<CommerceVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CommerceVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.commerce_item_adapter, parent, false)
        return CommerceVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: CommerceVH, position: Int) {
        val commerce = items[position]
        viewHolder.setData(commerce, clickListener)
    }

    fun addItemList(list: List<Commerce>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}