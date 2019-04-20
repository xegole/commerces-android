package com.webster.commerces.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.adapter.ImagePagerAdapter
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

            labelAddress.text = commerce.address
            labelDescription.text = commerce.description
            fabPhone.setOnClickListener { goToContact(commerce.phone.toString()) }
            viewPagerImages.adapter = ImagePagerAdapter(applicationContext, commerce.images)
            viewPagerImages.setEnableSwipe(true)
        }
    }

    private fun goToContact(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }
}
