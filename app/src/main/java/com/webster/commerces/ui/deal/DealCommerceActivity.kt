package com.webster.commerces.ui.deal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.activities.RESULT_CODE_GALLERY
import com.webster.commerces.databinding.ActivityDealCommerceBinding
import com.webster.commerces.utils.Constants
import kotlinx.android.synthetic.main.activity_deal_commerce.*

const val EXTRA_COMMERCE_ID = "extra_commerce_id"

class DealCommerceActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(DealCommerceViewModel::class.java)
    }
    private lateinit var binding: ActivityDealCommerceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_deal_commerce)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.viewGallery.setOnClickListener { onGalleryClick() }
        initObserver()
    }

    private fun initObserver() {
        viewModel.liveDataSuccess.observe(this, Observer { success ->
            if (success) {
                showSnack(R.string.message_created_deal_success)
            } else {
                showSnack(R.string.message_create_deal_error)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_admin_commerce, menu)
        return super.onCreateOptionsMenu(menu)
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
}