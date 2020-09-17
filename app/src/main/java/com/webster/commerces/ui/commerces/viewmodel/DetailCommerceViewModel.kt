package com.webster.commerces.ui.commerces.viewmodel

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.CommerceLocation
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.deal_item_adapter.view.*


class DetailCommerceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    val liveDataSocialAction = MutableLiveData<Intent>()
    val liveDataErrorMessage = MutableLiveData<Int>()
    val liveDataDescription = MutableLiveData<String>()
    val liveDataAddress = MutableLiveData<String>()
    val liveDataNameCommerce = MutableLiveData<String>()

    fun initCommerceData(commerce: Commerce) {
        liveDataAddress.value = commerce.address
        liveDataDescription.value = commerce.description
        liveDataNameCommerce.value = commerce.name
    }

    fun onClickItemMenu(menuItem: MenuItem, commerce: Commerce) {
        when (menuItem.itemId) {
            R.id.action_facebook -> goToFacebook(commerce.facebook)
            R.id.action_whatsapp -> goToChatWhatsapp(commerce.whatsapp)
            R.id.action_instagram -> goToInstagram(commerce.instagram)
            R.id.action_web_page -> goWebPage(commerce.webPage)
            R.id.action_email -> goSendEmail(commerce.email)
        }
    }

    fun onClickSeeMore(labelDescription: TextView) = View.OnClickListener {
        val labelSeeMore = it as TextView
        if (labelDescription.maxLines == 3) {
            labelDescription.maxLines = 200
            labelSeeMore.setText(R.string.label_see_less)
        } else {
            labelDescription.maxLines = 3
            labelSeeMore.setText(R.string.label_see_more)
        }
    }

    fun goToContact(phone: Long) = View.OnClickListener {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        liveDataSocialAction.value = intent
    }

    fun goToGoogleMap(commerceLocation: CommerceLocation?) = View.OnClickListener {
        commerceLocation?.run {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?&daddr=${commerceLocation.latitude},${commerceLocation.longitude}")
            )
            liveDataSocialAction.value = intent
        }
    }

    private fun goToChatWhatsapp(num: String) {
        val isAppInstalled = appInstalledOrNot("com.whatsapp")
        if (isAppInstalled) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+57$num"))
            liveDataSocialAction.value = intent
        } else {
            liveDataErrorMessage.value = R.string.error_action_no_whatsapp
        }
    }

    private fun goToInstagram(profile: String) {
        val isAppInstalled = appInstalledOrNot("com.instagram.android")
        if (isAppInstalled) {
            val profileIg = profile.replace("http://instagram.com/","").replace("@","")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$profileIg"))
            liveDataSocialAction.value = intent
        } else {
            liveDataErrorMessage.value = R.string.error_action_no_instagram
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = getApplication<AppCore>().packageManager
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$facebook"))
            liveDataSocialAction.value = intent
        } else {
            liveDataErrorMessage.value = R.string.error_action_no_facebook
        }
    }

    private fun goWebPage(url: String) {
        try {
            val openUrl = if (!url.startsWith("https://") && !url.startsWith("http://")) {
                "http://$url"
            } else {
                url
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(openUrl)
            liveDataSocialAction.value = Intent.createChooser(intent, "Abrir pagina web")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goSendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_EMAIL, email)
        }
        liveDataSocialAction.value = Intent.createChooser(intent, "Enviar correo")
    }

    fun verifyCommerce(commerceId: String, success: () -> Unit) {
        val map = HashMap<String, Any>()
        map["verified"] = true
        commercesReference.child(commerceId).updateChildren(map).addOnSuccessListener {
            success.invoke()
        }
    }
}