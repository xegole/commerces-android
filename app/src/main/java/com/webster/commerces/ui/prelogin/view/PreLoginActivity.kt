package com.webster.commerces.ui.prelogin.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.ui.prelogin.viewmodel.PreLoginViewModel

class PreLoginActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PreLoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }
}