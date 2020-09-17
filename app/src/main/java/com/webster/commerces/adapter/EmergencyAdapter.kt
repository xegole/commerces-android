package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.EmergencyVH
import com.webster.commerces.entity.Emergency

class EmergencyAdapter(
    private val items: ArrayList<Emergency> = arrayListOf(),
    private val onClickItem: ((Emergency) -> Unit)? = null
) :
    RecyclerView.Adapter<EmergencyVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): EmergencyVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_item_adapter, parent, false)
        return EmergencyVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: EmergencyVH, position: Int) {
        viewHolder.setData(items[position], onClickItem)
    }

    fun addItemList(list: List<Emergency>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}