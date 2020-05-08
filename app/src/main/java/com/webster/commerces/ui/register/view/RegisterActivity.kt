package com.webster.commerces.ui.register.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityRegisterBinding
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.register.model.ValidateUser
import com.webster.commerces.ui.register.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

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

        viewModel.liveDataError.observe(this, Observer {
            defaultErrorInputLayout()
            when (it) {
                ValidateUser.ERROR_EMAIL -> textFieldEmail.error =
                    getString(R.string.error_input_email)
                ValidateUser.ERROR_PHONE -> textFieldPhone.error =
                    getString(R.string.error_input_phone)
                ValidateUser.ERROR_PASSWORD -> textFieldPassword.error =
                    getString(R.string.error_input_password)
                ValidateUser.ERROR_VALIDATE_PASSWORD -> textFieldValidatePassword.error =
                    getString(R.string.error_input_password)
                ValidateUser.ERROR_VERIFY_PASSWORD -> textFieldValidatePassword.error =
                    getString(R.string.error_input_validate_password)
                ValidateUser.ERROR_EMAIL_USE -> textFieldEmail.error =
                    getString(R.string.error_input_email_in_use)
                else ->{
                }
            }
        })
    }

    private fun defaultErrorInputLayout() {
        textFieldEmail.error = null
        textFieldPhone.error = null
        textFieldPassword.error = null
        textFieldValidatePassword.error = null
    }
}