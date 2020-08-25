package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.base.BaseFragment

class ContactUsFragment : BaseFragment() {

    override fun resourceLayout() = R.layout.fragment_contact_us

    companion object {
        fun instance() = ContactUsFragment()
    }
}