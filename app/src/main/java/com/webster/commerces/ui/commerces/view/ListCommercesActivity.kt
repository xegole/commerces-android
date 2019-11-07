package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityListCommercesBinding
import com.webster.commerces.ui.commerces.ListCommercesViewModel
import kotlinx.android.synthetic.main.activity_list_commerces.*

class ListCommercesActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ListCommercesViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, v -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListCommercesBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_commerces)
        setSupportActionBar(toolbar)
        homeAsUpEnable()

        binding.viewModel = viewModel
        binding.recyclerCommerces.adapter = adapter
        viewModel.loadListCommerces()
        showLoading()
        initObservers()
    }

    private fun initObservers() {
        viewModel.commercesData.observe(this, Observer {
            adapter.addItemList(it)
            dismissLoading()
        })
    }
}