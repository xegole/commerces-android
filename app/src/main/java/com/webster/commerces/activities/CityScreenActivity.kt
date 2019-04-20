package com.webster.commerces.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.City
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.utils.FirebaseReferences
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_city_screen.*


class CityScreenActivity : BaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val prefs by lazy {
        Prefs(this)
    }

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference(FirebaseReferences.CITIES)

    private var city: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_screen)
        showLoading()

        myRef.addListDataListener<City> { list, success ->
            if (success) {
                setSpinnerWithCities(list)
            }
            dismissLoading()
        }
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities!!.onItemSelectedListener = this
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities!!.adapter = adapterCities
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        city = parent?.getItemAtPosition(position) as City
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do nothing
    }

    override fun onClick(v: View?) {
        if (checkBoxRememberCity.isChecked) {
            city?.run {
                prefs.cityId = cityId
            }
        }
        city?.run {
            goToActivity(HomeScreenActivity::class.java)
        }
    }
}
