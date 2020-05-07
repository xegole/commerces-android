package com.webster.commerces.ui.cityselector

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.activities.HomeScreenActivity
import com.webster.commerces.entity.City
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.ui.admin.AdminActivity
import com.webster.commerces.ui.commerces.view.AdminCommerceActivity
import com.webster.commerces.utils.FirebaseReferences

class CitySelectorViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val citiesReference = firebaseDatabase.getReference(FirebaseReferences.CITIES)

    val citiesData = MutableLiveData<List<City>>()
    val cityPrefsData = MutableLiveData<City>()

    fun onClickAdmin() = View.OnClickListener {
        it.goActivity(AdminActivity::class.java)
    }

    fun onClickAdminCommerce() = View.OnClickListener {
        it.goActivity(AdminCommerceActivity::class.java)
    }

    fun onClickDone(city: City?) = View.OnClickListener {
        city?.apply {
            cityPrefsData.value = this
            it.goActivity(HomeScreenActivity::class.java, true)
        }
    }

    fun loadCitiesByFirebase() {
        citiesReference.addListDataListener<City> { list, success ->
            citiesData.value = if (success) list else emptyList()
        }
    }
}