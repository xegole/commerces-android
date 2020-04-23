package com.webster.commerces.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_detail_commerce.*


class DetailCommerceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COMMERCE_DATA = "extra_commerce_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_commerce)
        setSupportActionBar(toolbar)
        supportPostponeEnterTransition()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        intent.extras?.let {
            val commerce = it.getSerializable(EXTRA_COMMERCE_DATA) as Commerce
            supportActionBar?.title = commerce.name

            imageCommerce.loadUrl(commerce.urlImage()) {
                onSuccess {
                    supportStartPostponedEnterTransition()
                }
                onError {
                    supportStartPostponedEnterTransition()
                    toolbarLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }
}
