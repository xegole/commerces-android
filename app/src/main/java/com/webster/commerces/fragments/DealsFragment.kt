package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.DealsAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.Deal
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_deals.*

class DealsFragment : BaseFragment() {
    private val database = FirebaseDatabase.getInstance()
    private val dealsReference = database.getReference(FirebaseReferences.DEALS)
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    private val adapter by lazy {
        DealsAdapter(showCommerce = true) { deal, showLabel ->
            if (showLabel) {
                goToCommerce(deal.commerce)
            }
        }
    }

    override fun resourceLayout() = R.layout.fragment_deals

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerDeals.adapter = adapter
        dealsReference.addListDataListener<Deal> { list, success ->
            adapter.setData(if (success) list else emptyList())
        }
    }

    companion object {
        fun instance() = DealsFragment()
    }

    private fun goToCommerce(commerceId: String) {
        commercesReference.orderByChild("commerceId").equalTo(commerceId)
            .addListDataListener<Commerce> { list, _ ->
                val commerce = list.firstOrNull()
                commerce?.let {
                    intentCommerce(it)
                }
            }
    }

    private fun intentCommerce(commerce: Commerce) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(bundle, DetailCommerceActivity::class.java)
    }
}