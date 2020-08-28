package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityListCommercesBinding
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.viewmodel.ListCommercesViewModel
import kotlinx.android.synthetic.main.activity_list_commerces.*

class ValidateCommercesActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(ListCommercesViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, _ ->
            val extras = Bundle()
            extras.putSerializable(EXTRA_COMMERCE_DATA, commerce)
            extras.putBoolean(EXTRA_VALIDATE_COMMERCE, true)
            openActivityWithBundle(extras, DetailCommerceActivity::class.java, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListCommercesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_list_commerces)
        setSupportActionBar(toolbar)
        homeAsUpEnable()

        binding.viewModel = viewModel
        binding.recyclerCommerces.adapter = adapter
        binding.lifecycleOwner = this
        viewModel.loadNonVerifiedCommerces()
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