package com.webster.commerces.activities

import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_detail_commerce.*
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*

class CategoryDetailActivity : BaseActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    companion object {
        const val EXTRA_CATEGORY_DATA = "extra_category_data"
    }

    private lateinit var commercesAdapter: CommercesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        intent.extras?.let { bundle ->
            val category = bundle.getSerializable(EXTRA_CATEGORY_DATA) as Category
            supportActionBar?.title = category.name

            commercesAdapter = CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
            recyclerCommerces.adapter = commercesAdapter

            showLoading()
            commercesReference.orderByChild("categoryId").equalTo(category.categoryId)
                .addListDataListener<Commerce> { list, success ->
                    if (success) {
                        commercesAdapter.addItemList(list)
                    }
                    dismissLoading()
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun commerceItemClicked(commerce: Commerce, view: View) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundleOptions(view, bundle, DetailCommerceActivity::class.java)
    }
}
