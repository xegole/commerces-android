package com.webster.commerces.adapter.viewholder

import android.content.Intent
import android.net.Uri
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

        itemView.buttonWhatsapp.setOnClickListener {
            goToChatWhatsapp(item.whatsapp)
        }

        itemView.buttonDial.setOnClickListener {
            goToContact(item.number)
        }

        itemView.imageEmergency.setImageResource(getImage(item.type))
    }

    private fun getImage(type: Int): Int {
        val drawableName = "ic_emergency_$type"
        return itemView.context.resources.getIdentifier(
            drawableName,
            "drawable",
            itemView.context.packageName
        )
    }

    private fun goToChatWhatsapp(num: String) {
        val isAppInstalled = appInstalledOrNot(itemView.context, "com.whatsapp")
        if (isAppInstalled) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+57$num"))
            itemView.context.startActivity(intent)
        }
    }

    private fun goToContact(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        itemView.context.startActivity(intent)
    }
}