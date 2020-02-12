package com.webster.commerces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.webster.commerces.R
import com.webster.commerces.adapter.viewholder.CategoryVH
import com.webster.commerces.entity.Category


class CategoryAdapter(
    private var items: ArrayList<Category>,
    private val clickListener: (Category) -> Unit ) :
    RecyclerView.Adapter<CategoryVH>(), Filterable {

    var filteredList: ArrayList<Category> = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_adapter, parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: CategoryVH, position: Int) {
        viewHolder.setData(items[position], clickListener)
    }

    fun addItemList(list: List<Category>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return itemsFilter
    }

    private val itemsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults? {
            filteredList.clear()
            val results = FilterResults()
            if (constraint.isEmpty()) {
                filteredList.addAll(items)
            } else {
                val filterPattern: String = constraint.toString().toLowerCase().trim()
                for (items in items) {
                    if (items.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(items)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size
            return results
        }
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            results.values
            notifyDataSetChanged()
        }
    }
}