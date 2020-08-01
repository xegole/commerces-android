package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.EmergencyAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Emergency
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.FirebaseReferences
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.fragment_emergency.*

private const val CITY_PARAMS = "cityId"

class EmergencyFragment : BaseFragment() {
    private val database = FirebaseDatabase.getInstance()
    private val emergencyReference = database.getReference(FirebaseReferences.EMERGENCY)

    override fun resourceLayout() = R.layout.fragment_emergency

    private val adapter by lazy {
        EmergencyAdapter(ArrayList())
    }

    private val prefs by lazy {
        Prefs(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerEmergency.adapter = adapter
        showLoading()
        emergencyReference.orderByChild(CITY_PARAMS).equalTo(prefs.cityId).addListDataListener<Emergency> { list, success ->
            if (success) {
                adapter.addItemList(list)
            }
            dismissLoading()
        }
    }

    companion object {
        fun instance() = EmergencyFragment()
    }
}