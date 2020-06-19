package com.webster.commerces.ui.deal.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.activities.RESULT_CODE_GALLERY
import com.webster.commerces.adapter.DealsAdapter
import com.webster.commerces.databinding.ActivityDealCommerceBinding
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.Deal
import com.webster.commerces.ui.commerces.view.EXTRA_COMMERCE_DATA
import com.webster.commerces.ui.deal.viewmodel.DealCommerceViewModel
import com.webster.commerces.utils.Constants
import kotlinx.android.synthetic.main.activity_deal_commerce.*


class DealCommerceActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(DealCommerceViewModel::class.java)
    }
    private lateinit var binding: ActivityDealCommerceBinding

    private var editDealDialog: EditDealDialog? = null

    private val commerce by lazy {
        intent.getSerializableExtra(EXTRA_COMMERCE_DATA) as Commerce
    }

    private val adapter by lazy {
        DealsAdapter { deal, _ ->
            editDealDialog = EditDealDialog(this) {
                when (it) {
                    EditDealState.EDIT -> showEdit(deal)
                    EditDealState.DELETE -> deleteDealDialog(deal.id)
                }
            }
            editDealDialog?.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_deal_commerce)
        binding.viewModel = viewModel
        binding.commerce = commerce
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.viewGallery.setOnClickListener { onGalleryClick() }
        binding.recyclerDeals.adapter = adapter
        initObserver()
    }

    private fun initObserver() {
        viewModel.getDealsByCommerce(commerce.commerceId)
        viewModel.liveDataSuccess.observe(this, Observer { success ->
            if (success) {
                showSnack(R.string.message_created_deal_success)
                binding.imageDeal.setImageDrawable(null)
                binding.recyclerDeals.visibility = View.VISIBLE
                binding.containerForm.visibility = View.GONE
            } else {
                showSnack(R.string.message_create_deal_error)
            }
        })

        viewModel.liveDataDeals.observe(this, Observer {
            if (it.isEmpty()) {
                binding.containerForm.visibility = View.VISIBLE
                binding.recyclerDeals.visibility = View.GONE
                binding.buttonDeal.setText(R.string.label_create_deal)
            } else {
                binding.recyclerDeals.visibility = View.VISIBLE
                binding.containerForm.visibility = View.GONE
                adapter.setData(it)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun onGalleryClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), RESULT_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                val selectedImageUri = data?.data ?: Uri.parse(Constants.EMPTY_STRING)
                viewModel.imageFile = selectedImageUri
                Picasso.get().load(selectedImageUri).into(imageDeal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showSnack(resId: Int) {
        Snackbar.make(binding.imageView2, resId, Snackbar.LENGTH_SHORT).show()
    }

    private fun showEdit(deal: Deal) {
        Picasso.get().load(deal.image).into(binding.imageDeal)
        binding.containerForm.visibility = View.VISIBLE
        binding.recyclerDeals.visibility = View.GONE
        binding.buttonDeal.setText(R.string.label_update_deal)
        viewModel.setEdit(deal)
        editDealDialog?.dismiss()
    }

    private fun deleteDealDialog(dealId: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.message_delete_deal_dialog))
        alertDialog.setPositiveButton(R.string.label_delete) { _, _ ->
            viewModel.deleteDealById(dealId)
            editDealDialog?.dismiss()
        }
        alertDialog.setNegativeButton(R.string.label_cancel) { _, _ ->
            editDealDialog?.dismiss()
        }
        alertDialog.show()
        alertDialog.create()
    }
}