package com.webster.commerces.fragments

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.activities.CategoryDetailActivity
import com.webster.commerces.activities.CategoryDetailActivity.Companion.EXTRA_CATEGORY_DATA
import com.webster.commerces.adapter.CategoryDealAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryDealsFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)
    private var mContext: Context? = null

    private val adapter by lazy {
        CategoryDealAdapter(ArrayList()) { category -> categoryItemClicked(category) }
    }

    override fun resourceLayout() = R.layout.fragment_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = this.activity
        setHasOptionsMenu(true)
        recyclerCategories.adapter = adapter
        showLoading()
        categoriesReference.addListDataListener<Category> { list, success ->
            if (success) {
                adapter.addItemList(list.sortedBy { it.name })
            }
            dismissLoading()
        }
    }

    private fun categoryItemClicked(category: Category) {
        val extras = Bundle()
        extras.putSerializable(EXTRA_CATEGORY_DATA, category)
        openActivityWithBundle(extras, CategoryDetailActivity::class.java)
    }

    companion object {
        fun instance() = CategoryDealsFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_search_view_menu, menu)
        val searchItem = menu.findItem(R.id.search_view)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter.filter.filter(newText)
        return true
    }
}