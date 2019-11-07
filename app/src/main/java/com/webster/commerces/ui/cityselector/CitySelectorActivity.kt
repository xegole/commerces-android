package com.webster.commerces.ui.cityselector

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityCityScreenBinding
import com.webster.commerces.entity.City
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_city_screen.*

class CitySelectorActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private val prefs by lazy {
        Prefs(this)
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CitySelectorViewModel::class.java)
    }

    private lateinit var binding: ActivityCityScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_screen)
        binding.viewModel = viewModel
        showLoading()
        viewModel.loadCitiesByFirebase()
        viewModel.citiesData.observe(this, Observer {
            setSpinnerWithCities(it)
            dismissLoading()
        })

        viewModel.cityPrefsData.observe(this, Observer {
            if (checkBoxRememberCity.isChecked) {
                prefs.cityId = it.cityId
            }
        })
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities.onItemSelectedListener = this
        spinnerCities.adapter = adapterCities
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.city = parent?.getItemAtPosition(position) as City
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
