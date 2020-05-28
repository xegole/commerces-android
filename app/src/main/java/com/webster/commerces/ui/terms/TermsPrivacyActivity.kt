package com.webster.commerces.ui.terms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.webster.commerces.R
import kotlinx.android.synthetic.main.activity_maps.toolbar
import kotlinx.android.synthetic.main.activity_terms_privacy.*

const val EXTRA_ASSETS_PAGE = "extra_assets_page"

class TermsPrivacyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_privacy)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val page = intent.extras?.getString(EXTRA_ASSETS_PAGE) ?: ""
        webViewPage.loadUrl("file:///android_res/raw/$page.html")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}