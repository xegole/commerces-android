package com.webster.commerces.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.entity.Deal
import kotlinx.android.synthetic.main.commerce_item_adapter.view.imageCommerce
import kotlinx.android.synthetic.main.commerce_item_adapter.view.labelName
import kotlinx.android.synthetic.main.deal_item_adapter.view.*
import java.util.*

class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(showLabel: Boolean, deal: Deal, clickListener: (Deal, Boolean) -> Unit) {
        itemView.run {
            labelName.text = deal.name
            labelDescription.text = deal.description
            labelPrice.text = String.format(Locale.getDefault(), "$${deal.price}")
            if (deal.image.isNotBlank()) {
                Picasso.get().load(deal.image).into(itemView.imageCommerce)
            }
            labelGoToCommerce.setOnClickListener { clickListener(deal, showLabel) }
            labelGoToCommerce.visibility = if (showLabel) View.VISIBLE else View.GONE

            labelSeeMore.setOnClickListener {
                if (labelDescription.maxLines == 3) {
                    labelDescription.maxLines = 10
                    labelSeeMore.setText(R.string.label_see_less)
                } else {
                    labelDescription.maxLines = 3
                    labelSeeMore.setText(R.string.label_see_more)
                }
            }
        }
    }
}