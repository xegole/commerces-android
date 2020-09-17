package com.webster.commerces.ui.emergency.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.adapter.EmergencyAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityEmergencyListBinding
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.emergency.viewmodel.EmergencyListViewModel

class EmergencyListActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(EmergencyListViewModel::class.java)
    }

    private val adapter by lazy {
        EmergencyAdapter(ArrayList()) { emergency ->
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("¿Desea eliminar o editar la emergencia?")
            alertDialog.setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteEmergency(emergency.id)
            }
            alertDialog.setNegativeButton("Editar") { _, _ ->
                val extras = Bundle()
                extras.putSerializable(EXTRA_EMERGENCY, emergency)
                openActivityWithBundle(extras, CreateEmergencyActivity::class.java, false)
            }
            alertDialog.show()
            alertDialog.create()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEmergencyListBinding>(
            this,
            R.layout.activity_emergency_list
        )
        binding.lifecycleOwner = this
        binding.recyclerEmergency.adapter = adapter
        setSupportActionBar(binding.toolbar)
        homeAsUpEnable()
        viewModel.loadEmergencies()
        initObservers()
    }

    private fun initObservers() {
        viewModel.emergenciesLiveData.observe(this) {
            adapter.addItemList(it)
        }
    }
}