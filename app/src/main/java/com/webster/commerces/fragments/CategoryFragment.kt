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
import com.webster.commerces.activities.EXTRA_CATEGORY_DATA
import com.webster.commerces.adapter.CategoryAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)
    private var mContext: Context? = null

    private lateinit var categoryAdapter: CategoryAdapter

    override fun resourceLayout() = R.layout.fragment_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = this.activity
        setHasOptionsMenu(true)
        categoryAdapter = CategoryAdapter(ArrayList()) { category -> categoryItemClicked(category) }
        recyclerCategories.adapter = categoryAdapter
        showLoading()
        categoriesReference.addListDataListener<Category> { list, success ->
            if (success) {
                categoryAdapter.addItemList(list.sortedBy { it.name })
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
        fun instance() = CategoryFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_search_view_menu, menu)
        val searchItem = menu.findItem(R.id.search_view)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String) = false

    override fun onQueryTextChange(newText: String): Boolean {
        categoryAdapter.filter.filter(newText)
        return true
    }
}