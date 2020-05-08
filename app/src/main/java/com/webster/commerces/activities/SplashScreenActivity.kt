package com.webster.commerces.activities

import android.os.Bundle
import android.os.Handler
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.cityselector.view.CitySelectorActivity
import com.webster.commerces.ui.login.view.LoginActivity
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.Prefs

class SplashScreenActivity : BaseActivity() {

    private val prefs by lazy {
        Prefs(this)
    }

    private val handler = Handler()
    private val runnable = Runnable {
        if (prefs.user == null) {
            goToActivity(LoginActivity::class.java)
        } else {
            goToActivity(CitySelectorActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
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
