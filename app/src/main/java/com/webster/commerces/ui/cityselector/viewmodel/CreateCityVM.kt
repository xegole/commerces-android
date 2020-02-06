package com.webster.commerces.ui.cityselector.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.entity.City
import com.webster.commerces.extensions.getString
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

class CreateCityVM(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val referenceCities = database.getReference(FirebaseReferences.CITIES)

    val cityName = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

    fun saveCityToFirebase() = View.OnClickListener {
        val city = City()
        city.name = cityName.value ?: Constants.EMPTY_STRING
        referenceCities.push().setValue(city).addOnSuccessListener {
            successMessage.value = getString(R.string.message_city_created_success)
            cityName.value = Constants.EMPTY_STRING
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }
}