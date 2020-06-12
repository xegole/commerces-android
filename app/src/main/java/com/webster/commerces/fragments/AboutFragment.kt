package com.webster.commerces.fragments

import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.utils.FirebaseReferences

class AboutFragment : BaseFragment() {
    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    override fun resourceLayout() = R.layout.fragment_about

    companion object {
        fun instance() = AboutFragment()
    }
}