package com.webster.commerces.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.BuildConfig
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityLoginBinding
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.login.model.UserLogin
import com.webster.commerces.ui.login.viewmodel.LoginViewModel
import com.webster.commerces.ui.terms.EXTRA_ASSETS_PAGE
import com.webster.commerces.ui.terms.TermsPrivacyActivity
import kotlinx.android.synthetic.main.activity_login.*


const val RC_SIGN_IN = 203
const val USERS_DATABASE = "users"

class LoginActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initGoogleSignInClient(this)
        initObserver()
    }

    private fun initObserver() {
        viewModel.registerSuccess.observe(this, Observer {
            goToActivity(it)
        })
        viewModel.liveDataError.observe(this, Observer {
            when (it) {
                UserLogin.ERROR_EMAIL -> textFieldEmail.error =
                    getString(R.string.error_input_email)
                UserLogin.ERROR_NOT_FOUND -> textFieldEmail.error =
                    getString(R.string.error_input_email_not_found)
                UserLogin.ERROR_PASSWORD -> textFieldPassword.error =
                    getString(R.string.error_input_password)
                UserLogin.ERROR_WRONG_PASSWORD -> textFieldPassword.error =
                    getString(R.string.error_input_wrong_password)
                UserLogin.ERROR_TOO_MANY_REQUESTS -> textFieldPassword.error =
                    getString(R.string.error_input_too_many_requests)
                else -> {
                    textFieldEmail.error = null
                    textFieldPassword.error = null
                }
            }
        })

        binding.labelTermsPrivacy.movementMethod = LinkMovementMethod.getInstance()

        if (BuildConfig.DEBUG) {
            //viewModel.initTestData()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        when {
            intent?.scheme.equals("terms") -> {
                val extras = Bundle()
                extras.putString(EXTRA_ASSETS_PAGE, "terms")
                openActivityWithBundle(extras, TermsPrivacyActivity::class.java, false)
            }
            intent?.scheme.equals("privacy") -> {
                val extras = Bundle()
                extras.putString(EXTRA_ASSETS_PAGE, "privacy")
                openActivityWithBundle(extras, TermsPrivacyActivity::class.java, false)
            }
            else -> {
                super.onNewIntent(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.onActivityResult(data)
        }
    }
}