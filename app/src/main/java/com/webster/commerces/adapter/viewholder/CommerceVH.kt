package com.webster.commerces.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import com.webster.commerces.entity.Commerce
import com.webster.commerces.services.RetrofitServices
import kotlinx.android.synthetic.main.commerce_item_adapter.view.*

class CommerceVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setData(commerce: Commerce, clickListener: (Commerce, View) -> Unit) {
        itemView.labelName.text = commerce.name
        itemView.labelAddress.text = commerce.address
        val urlImage = RetrofitServices.BASE_URL + commerce.commerceImage
        Picasso.get().load(urlImage).into(itemView.imageCommerce)
        itemView.setOnClickListener { clickListener(commerce, itemView.imageCommerce) }
    }
}