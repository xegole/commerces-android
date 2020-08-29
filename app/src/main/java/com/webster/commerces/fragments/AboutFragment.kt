package com.webster.commerces.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.extensions.appInstalledOrNot
import kotlinx.android.synthetic.main.fragment_about.*

private const val PROFILE = "lobuscaloencuentras"

class AboutFragment : BaseFragment() {
    override fun resourceLayout() = R.layout.fragment_about

    companion object {
        fun instance() = AboutFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonFacebook.setOnClickListener {
            goToFacebook()
        }
        buttonInstagram.setOnClickListener {
            goToInstagram()
        }
        buttonYoutube.setOnClickListener {
            goYoutube()
        }
        buttonWebPage.setOnClickListener {
            goWebPage()
        }
    }

    private fun goToFacebook() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.facebook.com/lobuscaloencuentras")
            startActivity(Intent.createChooser(intent, "Abrir pagina web"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goWebPage() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://merka.webster-inc.com")
            startActivity(Intent.createChooser(intent, "Abrir pagina web"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goYoutube() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/channel/UCcd89Pro_TZEcdEypdfyOCA?view")
            startActivity(Intent.createChooser(intent, "Abrir pagina web"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goToInstagram() {
        val isAppInstalled = appInstalledOrNot("com.instagram.android")
        if (isAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$PROFILE"))
            startActivity(intent)
        }
    }
}