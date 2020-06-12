package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_emergency.*

class EmergencyFragment : BaseFragment() {
    private val database = FirebaseDatabase.getInstance()
    private val emergencyReference = database.getReference(FirebaseReferences.EMERGENCY)

    override fun resourceLayout() = R.layout.fragment_emergency

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerEmergency.adapter = adapter
        showLoading()
        emergencyReference.addListDataListener<Commerce> { list, success ->
            if (success) {
                adapter.addItemList(list)
            }
            dismissLoading()
        }
    }

    private fun commerceItemClicked(commerce: Commerce, view: View) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundleOptions(view, bundle, DetailCommerceActivity::class.java)
    }

    companion object {
        fun instance() = EmergencyFragment()
    }
}