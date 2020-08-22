package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.activities.UpdateCommerceActivity
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityListCommercesBinding
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.commerces.viewmodel.ListCommercesViewModel
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_list_commerces.*

class ListCommercesActivity : BaseActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    private val viewModel by lazy {
        ViewModelProvider(this).get(ListCommercesViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, _ ->
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("¿DO YOU WANT DELETE A COMMERCE OR EDIT?")
            alertDialog.setPositiveButton("DELETE") { _, _ ->
                deleteCommerce(commerce.commerceId)
            }
            alertDialog.setNegativeButton("EDIT") { _, _ ->
                goToActivity(UpdateCommerceActivity::class.java)
            }
            alertDialog.show()
            alertDialog.create()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListCommercesBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_list_commerces)
        setSupportActionBar(toolbar)
        homeAsUpEnable()

        binding.viewModel = viewModel
        binding.recyclerCommerces.adapter = adapter
        binding.lifecycleOwner = this
        viewModel.loadListCommerces()
        showLoading()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_update_commerce_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.updateData -> {
                goToActivity(UpdateCommerceActivity::class.java)
            }
        }
        return true
    }


    private fun initObservers() {
        viewModel.commercesData.observe(this, Observer {
            adapter.addItemList(it)
            dismissLoading()
        })
    }

    private fun deleteCommerce(id: String) {
        val commerce = Commerce()
        commerce.commerceId = id
        commercesReference.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Elliminado", Toast.LENGTH_LONG).show()
        }
    }
}