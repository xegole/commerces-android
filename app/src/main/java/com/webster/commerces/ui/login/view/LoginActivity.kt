package com.webster.commerces.ui.login.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityLoginBinding
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.entity.User
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.cityselector.CitySelectorActivity
import com.webster.commerces.ui.commerces.view.AdminCommerceActivity
import com.webster.commerces.ui.login.viewmodel.LoginViewModel
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_create_commerce.*
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
        viewModel.initGoogleSignInClient(this)
        viewModel.registerSuccess.observe(this, Observer {
            goToActivity(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.onActivityResult(data)
        }
    }
}