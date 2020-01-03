package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityListCommercesBinding
import com.webster.commerces.entity.Commerce
import com.webster.commerces.ui.commerces.ListCommercesViewModel
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_list_commerces.*

class ListCommercesActivity : BaseActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ListCommercesViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, v ->
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("TITLE")
            alertDialog.setPositiveButton("YES") { dialog, which ->
                deleteCommerce(commerce.commerceId)
            }
            alertDialog.setNegativeButton("NO"){ dialog, which ->
                Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()
            }
            alertDialog.show()
            alertDialog.create()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListCommercesBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_commerces)
        setSupportActionBar(toolbar)
        homeAsUpEnable()

        binding.viewModel = viewModel
        binding.recyclerCommerces.adapter = adapter
        viewModel.loadListCommerces()
        showLoading()
        initObservers()
    }

    private fun initObservers() {
        viewModel.commercesData.observe(this, Observer {
            adapter.addItemList(it)
            dismissLoading()
        })
    }

    private fun deleteCommerce(id: String){
        val commerce = Commerce()
        commerce.commerceId = id
        commercesReference.child(id).removeValue()
    }
}