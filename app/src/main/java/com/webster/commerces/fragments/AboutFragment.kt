package com.webster.commerces.fragments

import com.webster.commerces.R
import com.webster.commerces.base.BaseFragment

class AboutFragment : BaseFragment() {

    override fun resourceLayout() = R.layout.fragment_about

    companion object {
        fun instance() = AboutFragment()
    }
}