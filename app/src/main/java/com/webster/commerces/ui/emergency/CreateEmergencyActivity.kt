package com.webster.commerces.ui.emergency

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.webster.commerces.R
import com.webster.commerces.base.BaseSpinnerActivity
import com.webster.commerces.databinding.ActivityCreateEmergencyBinding
import com.webster.commerces.entity.City
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.ui.emergency.viewmodel.CreateEmergencyViewModel
import kotlinx.android.synthetic.main.activity_create_commerce.*

class CreateEmergencyActivity : BaseSpinnerActivity<City>() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateEmergencyViewModel::class.java)
    }

    private lateinit var binding: ActivityCreateEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_emergency)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        homeAsUpEnable()
        setObservers()
    }

    private fun setObservers() {
        viewModel.loadData()
        viewModel.citiesLiveData.observe(this, Observer {
            setSpinnerWithCities(it)
        })
        viewModel.typeLiveData.observe(this, Observer {
            viewModel.showDialNumber.value = it == 5
        })

        viewModel.successLiveData.observe(this, Observer {
            binding.buttonCreate.hideKeyboard()
            if (it.isEmpty()) {
                Snackbar.make(
                    binding.buttonCreate,
                    R.string.message_created_emergency_success,
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.emergencyNumberLiveData.value = ""
                viewModel.emergencyWhatsappLiveData.value = ""
                viewModel.dialNumberLiveData.value = ""
            } else {
                Snackbar.make(binding.buttonCreate, it, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities?.onItemSelectedListener = this
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities?.adapter = adapterCities
    }

    override fun onItemSelected(item: City) {
        viewModel.city = item
    }
}