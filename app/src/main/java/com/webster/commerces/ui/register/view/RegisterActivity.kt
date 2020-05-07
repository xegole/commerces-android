package com.webster.commerces.ui.register.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityRegisterBinding
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.register.viewmodel.RegisterViewModel

class RegisterActivity : BaseActivity() {

    val viewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(
            this,
            R.layout.activity_register
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initObservers()
    }

    private fun initObservers() {
        viewModel.liveDataRegisterSuccess.observe(this, Observer {
            goToActivity(it)
        })
    }
}