package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.DealViewHolder
import com.webster.commerces.entity.Deal

class DealsAdapter(
    private val items: ArrayList<Deal> = arrayListOf(),
    private val showCommerce: Boolean = false,
    private val clickListener: (Deal, Boolean) -> Unit
) : RecyclerView.Adapter<DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.deal_item_adapter, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: DealViewHolder, position: Int) {
        viewHolder.setData(showCommerce, items[position], clickListener)
    }

    fun setData(list: List<Deal>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}