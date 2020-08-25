package com.webster.commerces.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_detail_commerce.*
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*

const val EXTRA_CATEGORY_DATA = "extra_category_data"
const val EXTRA_CATEGORY_DEAL = "extra_category_deal"

private const val CATEGORY_ID = "categoryId"

class CategoryDetailActivity : BaseActivity(), SearchView.OnQueryTextListener {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
    }

    private var openFromDeals = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        intent.extras?.let { bundle ->
            val category = bundle.getSerializable(EXTRA_CATEGORY_DATA) as Category
            openFromDeals = bundle.getBoolean(EXTRA_CATEGORY_DEAL, false)
            supportActionBar?.title = category.name
            recyclerCommerces.adapter = adapter

            showLoading()
            commercesReference.orderByChild(CATEGORY_ID).equalTo(category.categoryId)
                .addListDataListener<Commerce> { list, success ->
                    if (success) {
                        adapter.addItemList(list.filter { it.verified })
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
        openActivityWithBundleOptions(
            view,
            bundle,
            if (openFromDeals) DealsActivity::class.java else DetailCommerceActivity::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_search_view_menu, menu)
        val searchItem = menu?.findItem(R.id.search_view)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return true
    }
}