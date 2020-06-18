package com.webster.commerces.activities

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.DealsAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.Deal
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_deals.*

private const val COMMERCE_ID = "commerce"

class DealsActivity : BaseActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val dealsReference = database.getReference(FirebaseReferences.DEALS)

    private val commerce by lazy {
        intent.extras?.getSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA) as Commerce
    }

    private val adapter by lazy {
        DealsAdapter(showCommerce = true) { _, showLabel ->
            if (showLabel) {
                intentCommerce(commerce)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_deals)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        recyclerDeals.adapter = adapter
        dealsReference.orderByChild(COMMERCE_ID).equalTo(commerce.commerceId)
            .addListDataListener<Deal> { list, success ->
                adapter.setData(if (success) list else emptyList())
            }
    }

    companion object {
        fun instance() = DealsActivity()
    }

    private fun intentCommerce(commerce: Commerce) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(bundle, DetailCommerceActivity::class.java, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}