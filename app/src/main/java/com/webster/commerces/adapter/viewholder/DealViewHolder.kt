package com.webster.commerces.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.webster.commerces.entity.Deal
import kotlinx.android.synthetic.main.commerce_item_adapter.view.imageCommerce
import kotlinx.android.synthetic.main.commerce_item_adapter.view.labelAddress
import kotlinx.android.synthetic.main.commerce_item_adapter.view.labelName
import kotlinx.android.synthetic.main.deal_item_adapter.view.*

class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(showLabel: Boolean, deal: Deal, clickListener: (Deal, Boolean) -> Unit) {
        itemView.labelName.text = deal.name
        itemView.labelAddress.text = deal.description
        if (deal.image.isNotBlank()) {
            Picasso.get().load(deal.image).into(itemView.imageCommerce)
        }
        itemView.setOnClickListener { clickListener(deal, showLabel) }
        itemView.labelGoToCommerce.visibility = if (showLabel) View.VISIBLE else View.GONE
    }
}