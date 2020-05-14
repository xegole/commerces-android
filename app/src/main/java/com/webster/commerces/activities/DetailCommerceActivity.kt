package com.webster.commerces.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webster.commerces.R
import com.webster.commerces.adapter.ImagePagerAdapter
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_detail_commerce.*


class DetailCommerceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COMMERCE_DATA = "extra_commerce_data"
    }

    @SuppressLint("RestrictedApi")
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

            fabFacebook.visibility = if (commerce.facebook.isNotEmpty()) View.VISIBLE else View.GONE
            fabWhatsapp.visibility = if (commerce.whatsapp.isNotEmpty()) View.VISIBLE else View.GONE
            fabInstagram.visibility =
                if (commerce.instagram.isNotEmpty()) View.VISIBLE else View.GONE
            fabWebPage.visibility = if (commerce.webPage.isNotEmpty()) View.VISIBLE else View.GONE
            fabEmail.visibility = if (commerce.email.isNotEmpty()) View.VISIBLE else View.GONE

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
            fabLocation.setOnClickListener { goToGoogleMap() }
            fabHowLocation.setOnClickListener { goToGoogleMapLocation() }
            fabFacebook.setOnClickListener { goToFacebook(commerce.facebook) }
            fabWhatsapp.setOnClickListener { goToChatWhatsapp(commerce.whatsapp) }
            fabInstagram.setOnClickListener { goToInstagram(commerce.instagram) }
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

    private fun goToGoogleMap() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q=Parque Simon Bolivar, Cra. 3 #15-77, La Dorada, Caldas")
        )
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun goToGoogleMapLocation() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:0,0?q=Parque Simon Bolivar, Cra. 3 #15-77, La Dorada, Caldas")
        )
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun goToChatWhatsapp(num: String) {
        val isAppInstalled = appInstalledOrNot("com.whatsapp")
        if (isAppInstalled) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+57$num"))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No tiene whatsapp instalado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToInstagram(profile: String) {
        val isAppInstalled = appInstalledOrNot("com.instagram.android")
        if (isAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$profile"))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No tiene instagram instalado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun goToFacebook(facebook: String) {
        val isAppInstalled = appInstalledOrNot("com.facebook.katana")
        if (isAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/$facebook"))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No tiene facebook instalado", Toast.LENGTH_SHORT).show()
        }
    }
}