package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.activities.CategoryDetailActivity
import com.webster.commerces.activities.CategoryDetailActivity.Companion.EXTRA_CATEGORY_DATA
import com.webster.commerces.adapter.CategoryAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_category.*
import java.util.*

class CategoryFragment : BaseFragment() {

    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    private lateinit var categoryAdapter: CategoryAdapter

    override fun resourceLayout() = R.layout.fragment_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter(ArrayList()) { category -> categoryItemClicked(category) }
        recyclerCategories.adapter = categoryAdapter

        showLoading()
        categoriesReference.addListDataListener<Category> { list, success ->
            if (success) {
                categoryAdapter.addItemList(list)
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
}