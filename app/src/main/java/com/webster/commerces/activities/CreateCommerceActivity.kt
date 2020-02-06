package com.webster.commerces.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.databinding.ActivityCreateCommerceBinding
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.City
import com.webster.commerces.ui.commerces.adapter.PagerImagesAdapter
import com.webster.commerces.ui.commerces.viewmodel.CreateCommerceVM
import com.webster.commerces.utils.Constants
import kotlinx.android.synthetic.main.activity_create_category.viewGallery
import kotlinx.android.synthetic.main.activity_create_commerce.*

class CreateCommerceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateCommerceVM::class.java)
    }

    private val adapter by lazy {
        PagerImagesAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateCommerceBinding>(
            this,
            R.layout.activity_create_commerce
        )
        binding.viewModel = viewModel
        binding.pagerImages.adapter = adapter
        selected_country.enableHint(false)
        selected_country.registerPhoneNumberTextView(textWhatsapp)

        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewGallery.setOnClickListener {
            onGalleryClick()
        }

        viewModel.getCategories()
        viewModel.getCities()

        viewModel.commercesImages.observe(this, Observer {
            adapter.setData(it)
        })

        viewModel.listCategories.observe(this, Observer {
            setSpinnerWithCategories(it)
        })
        viewModel.listCities.observe(this, Observer {
            setSpinnerWithCities(it)
        })
    }

    private fun setSpinnerWithCategories(listCategories: List<Category>) {
        spinnerCategories?.onItemSelectedListener = this
        val adapterCategories = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategories)
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories?.adapter = adapterCategories
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities?.onItemSelectedListener = this
        val adapterCategories = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories?.adapter = adapterCategories
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSelected = parent?.getItemAtPosition(position)
        if (itemSelected is Category) {
            viewModel.category = itemSelected
        } else if (itemSelected is City) {
            viewModel.city = itemSelected
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do nothing
    }


    private fun onGalleryClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), RESULT_CODE_GALLERY)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                val selectedImageUri = data?.data ?: Uri.parse(Constants.EMPTY_STRING)
                viewModel.imageFile = selectedImageUri
                Picasso.get().load(selectedImageUri).into(imageCommerce)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
