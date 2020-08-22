package com.webster.commerces.ui.categories.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.CategoryAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityListCategoriesBinding
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.categories.ListCategoriesViewModel
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_list_categories.*

class ListCategoriesActivity : BaseActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    private val viewModel by lazy {
        ViewModelProvider(this).get(ListCategoriesViewModel::class.java)
    }

    private val adapter by lazy {
        CategoryAdapter(ArrayList()) { category ->
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("¿Que desea hacer con la categoria?")
            alertDialog.setPositiveButton("Editar") { _, _ ->
                val extras = Bundle()
                extras.putBoolean(EXTRA_EDIT_MODE_CATEGORY, true)
                extras.putSerializable(EXTRA_CATEGORY_DATA, category)
                openActivityWithBundle(extras, CreateCategoryActivity::class.java, false)
            }
            alertDialog.setNegativeButton("Eliminar") { _, _ ->
                deleteCategory(category.categoryId)
            }
            alertDialog.create()
            alertDialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListCategoriesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_list_categories)
        setSupportActionBar(toolbarCategories)
        homeAsUpEnable()

        binding.recyclerCategories.adapter = adapter
        viewModel.loadListCategories()
        showLoading()
        initObservers()
    }

    private fun initObservers() {
        viewModel.categoryData.observe(this, Observer {
            adapter.addItemList(it)
            dismissLoading()
            adapter.notifyDataSetChanged()
        })
    }

    private fun deleteCategory(id: String) {
        val category = Category()
        category.categoryId = id
        categoriesReference.child(id).removeValue()
    }
}