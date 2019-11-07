package com.webster.commerces.ui.admin

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityAdminBinding

class AdminActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AdminViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAdminBinding = DataBindingUtil.setContentView(this, R.layout.activity_admin)
        binding.viewModel = viewModel
        setSupportActionBar(binding.toolbar)
        homeAsUpEnable()
    }
}