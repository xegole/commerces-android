package com.webster.commerces.ui.cityselector.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.entity.City
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.ui.admin.AdminActivity
import com.webster.commerces.ui.commerces.view.AdminCommerceActivity
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.select_city_dialog.*

class SelectCityDialog(context: Context, private val callback: () -> Unit) :
    Dialog(context), AdapterView.OnItemSelectedListener {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val citiesReference = firebaseDatabase.getReference(FirebaseReferences.CITIES)

    private val prefs by lazy {
        AppCore.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_city_dialog)
        setCancelable(false)
        loadCitiesByFirebase()
        buttonDone.setOnClickListener {
            prefs.remember = checkBoxRememberCity.isChecked
            callback.invoke()
            dismiss()
        }

        buttonAdmin.visibility =
            if (prefs.user?.typeUser == TypeUser.ADMIN) View.VISIBLE else View.GONE

        buttonUser.visibility =
            if (prefs.user?.typeUser == TypeUser.USER_COMMERCE) View.VISIBLE else View.GONE

        buttonAdmin.setOnClickListener {
            val intent = Intent(context, AdminActivity::class.java)
            context.startActivity(intent)
            callback.invoke()
            dismiss()
        }

        buttonUser.setOnClickListener {
            val intent = Intent(context, AdminCommerceActivity::class.java)
            context.startActivity(intent)
            callback.invoke()
            dismiss()
        }
    }

    private fun loadCitiesByFirebase() {
        citiesReference.addListDataListener<City> { list, success ->
            val cities = if (success) list else emptyList()
            setSpinnerWithCities(cities)
        }
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        val adapterCities = ArrayAdapter(context, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities.onItemSelectedListener = this
        spinnerCities.adapter = adapterCities
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val citySelected = parent?.getItemAtPosition(position) as City
        prefs.cityId = citySelected.cityId
    }
}