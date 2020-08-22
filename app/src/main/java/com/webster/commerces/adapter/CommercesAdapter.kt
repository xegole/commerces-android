package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.CommerceVH
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.containsDeAccentLowCase
import java.util.*
import kotlin.collections.ArrayList

class CommercesAdapter(
    private val items: ArrayList<Commerce> = arrayListOf(),
    private val clickListener: (Commerce, View) -> Unit
) : RecyclerView.Adapter<CommerceVH>(), Filterable {

    var filteredList: ArrayList<Commerce> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CommerceVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.commerce_item_adapter, parent, false)
        return CommerceVH(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(viewHolder: CommerceVH, position: Int) {
        viewHolder.setData(filteredList[position], clickListener)
    }

    fun addItemList(list: List<Commerce>) {
        filteredList.clear()
        filteredList.addAll(list)
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return itemsFilter
    }

    private val itemsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults? {
            val results = FilterResults()
            val list = if (constraint.isEmpty()) {
                items
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                items.filter { it.name.containsDeAccentLowCase(filterPattern) }
            }
            results.values = list
            results.count = list.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (results.values is List<*>) {
                val list = results.values as List<Commerce>
                filteredList.clear()
                filteredList.addAll(list)
                notifyDataSetChanged()
            }
        }
    }
}