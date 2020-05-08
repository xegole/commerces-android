package com.webster.commerces.ui.cityselector.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.webster.commerces.R
import com.webster.commerces.databinding.ActivityCreateCityBinding
import com.webster.commerces.ui.cityselector.viewmodel.CreateCityVM
import kotlinx.android.synthetic.main.activity_create_city.*

class CreateCityActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateCityVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateCityBinding>(this, R.layout.activity_create_city)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.successMessage.observe(this, Observer {
            Snackbar.make(binding.textName, it, Snackbar.LENGTH_SHORT).show()
        })

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}