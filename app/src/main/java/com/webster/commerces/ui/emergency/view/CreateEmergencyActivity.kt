package com.webster.commerces.ui.emergency.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.webster.commerces.R
import com.webster.commerces.base.BaseSpinnerActivity
import com.webster.commerces.databinding.ActivityCreateEmergencyBinding
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Emergency
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.ui.emergency.viewmodel.CreateEmergencyViewModel
import kotlinx.android.synthetic.main.activity_create_commerce.*

const val EXTRA_EMERGENCY = "extra_emergency"

class CreateEmergencyActivity : BaseSpinnerActivity<City>() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateEmergencyViewModel::class.java)
    }

    private lateinit var binding: ActivityCreateEmergencyBinding

    private val emergency by lazy {
        intent.extras?.getSerializable(EXTRA_EMERGENCY) as? Emergency
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_emergency)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        homeAsUpEnable()
        setObservers()

        emergency?.apply {
            binding.buttonCreate.setText(R.string.label_update_emergency)
            viewModel.setInitialData(this)
        }
    }

    private fun setObservers() {
        viewModel.loadData()
        viewModel.citiesLiveData.observe(this, {
            setSpinnerWithCities(it)
        })

        viewModel.successLiveData.observe(this, {
            binding.buttonCreate.hideKeyboard()
            if (it.isEmpty()) {
                Snackbar.make(
                    binding.buttonCreate,
                    R.string.message_created_emergency_success,
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.clearFields()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities?.onItemSelectedListener = this
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities?.adapter = adapterCities

        emergency?.apply {
            val index = listCities.indexOfFirst { it.cityId == cityId }
            spinnerCities.setSelection(index)
            binding.spinnerEmergencies.setSelection(type)
        }
    }

    override fun onItemSelected(item: City) {
        viewModel.city = item
    }
}