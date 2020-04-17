package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.CategoryVH
import com.webster.commerces.entity.Category
import java.util.*
import kotlin.collections.ArrayList


class CategoryAdapter(
    private var items: ArrayList<Category>,
    private val clickListener: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoryVH>(), Filterable {

    var filteredList: ArrayList<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_adapter, parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(viewHolder: CategoryVH, position: Int) {
        viewHolder.setData(filteredList[position], clickListener)
    }

    fun addItemList(list: List<Category>) {
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
            val list = if (constraint.isEmpty()){
                items
            }else {
                val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                items.filter { it.name.toLowerCase(Locale.getDefault()).contains(filterPattern) }
            }
            results.values = list
            results.count = list.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (results.values is List<*>) {
                val list = results.values as List<Category>
                filteredList.clear()
                filteredList.addAll(list)
                notifyDataSetChanged()
            }
        }
    }
}