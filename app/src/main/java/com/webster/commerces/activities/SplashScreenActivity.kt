package com.webster.commerces.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.login.view.LoginActivity
import com.webster.commerces.ui.register.view.RegisterActivity
import com.webster.commerces.ui.tutorial.TutorialActivity
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_pre_login.*

class SplashScreenActivity : BaseActivity() {

    private val handler = Handler()
    private val runnable = Runnable {
        if (AppCore.prefs.firstTime) {
            goToActivity(TutorialActivity::class.java)
        } else {
            if (AppCore.prefs.user == null) {
                containerFlowLogin.visibility = View.VISIBLE
            } else {
                goToActivity(HomeScreenActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)
        buttonLogin.setOnClickListener {
            goToActivity(LoginActivity::class.java)
        }

        buttonSingUp.setOnClickListener {
            goToActivity(RegisterActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, Constants.DELAY_SPLASH)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
