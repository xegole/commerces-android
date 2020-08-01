package com.webster.commerces.adapter.viewholder

import android.view.View
import com.webster.commerces.R
import com.webster.commerces.adapter.base.BaseViewHolder
import com.webster.commerces.entity.Emergency
import kotlinx.android.synthetic.main.emergency_item_adapter.view.*

class EmergencyVH(itemView: View) : BaseViewHolder<Emergency>(itemView) {

    override fun setData(item: Emergency) {
        val typeArray = itemView.context.resources.getStringArray(R.array.emergency_types)
        itemView.labelName.text = typeArray[item.type]

        itemView.buttonDial.visibility = if (item.number.isEmpty()) View.GONE else View.VISIBLE
        itemView.buttonWhatsapp.visibility =
            if (item.whatsapp.isEmpty()) View.GONE else View.VISIBLE

        itemView.setOnClickListener {
            itemView.containerDial.visibility = View.VISIBLE
        }

        itemView.containerDial.setOnClickListener {
            itemView.containerDial.visibility = View.GONE
        }
    }
}