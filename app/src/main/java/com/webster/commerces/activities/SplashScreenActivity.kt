package com.webster.commerces.activities

import android.os.Bundle
import android.os.Handler
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.utils.Constants

class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({ goToActivity(CityScreenActivity::class.java) }, Constants.DELAY_SPLASH)
    }
}
