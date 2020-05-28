package com.webster.commerces.ui.commerces.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.activities.RESULT_CODE_GALLERY
import com.webster.commerces.databinding.ActivityCreateCommerceBinding
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.CommerceLocation
import com.webster.commerces.ui.commerces.adapter.PagerImagesAdapter
import com.webster.commerces.ui.commerces.viewmodel.CreateCommerceVM
import com.webster.commerces.ui.maps.EXTRA_COMMERCE_LOCATION
import com.webster.commerces.utils.Constants
import kotlinx.android.synthetic.main.activity_create_category.viewGallery
import kotlinx.android.synthetic.main.activity_create_commerce.*


const val EXTRA_EDIT_MODE = "extra_edit_mode"
const val EXTRA_COMMERCE_DATA = "extra_commerce_data"
const val REQUEST_COMMERCE_LOCATION = 201

class CreateCommerceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateCommerceVM::class.java)
    }

    private val adapter by lazy {
        PagerImagesAdapter(supportFragmentManager)
    }

    private val editMode by lazy {
        intent.extras?.getBoolean(EXTRA_EDIT_MODE, false) ?: false
    }

    private val commerceData by lazy {
        intent.extras?.getSerializable(EXTRA_COMMERCE_DATA) as Commerce?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateCommerceBinding>(
            this,
            R.layout.activity_create_commerce
        )
        binding.viewModel = viewModel
        binding.pagerImages.adapter = adapter
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)
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

        viewModel.commerceCreatedSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(
                    this,
                    getString(R.string.message_created_commerce_success),
                    Toast.LENGTH_SHORT
                ).show()
                onSupportNavigateUp()
            }
        })

        viewModel.liveDataIntent.observe(this, Observer {
            startActivityForResult(it, REQUEST_COMMERCE_LOCATION)
        })

        if (editMode) {
            commerceData?.let {
                viewModel.initEditMode(it)
            }
        }
    }

    private fun setSpinnerWithCategories(listCategories: List<Category>) {
        spinnerCategories?.onItemSelectedListener = this
        val adapterCategories =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategories)
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories?.adapter = adapterCategories

        if (editMode) {
            commerceData?.let { commerce ->
                val index = listCategories.indexOfFirst { it.categoryId == commerce.categoryId }
                spinnerCategories?.setSelection(index)
            }
        }
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities?.onItemSelectedListener = this
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities?.adapter = adapterCities

        if (editMode) {
            commerceData?.let { commerce ->
                val index = listCities.indexOfFirst { it.cityId == commerce.cityId }
                spinnerCities?.setSelection(index)
            }
        }
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
        startActivityForResult(
            Intent.createChooser(intent, "Select image"),
            RESULT_CODE_GALLERY
        )
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
        } else if (requestCode == REQUEST_COMMERCE_LOCATION && resultCode == Activity.RESULT_OK) {
            val location = data?.getParcelableExtra<LatLng>(EXTRA_COMMERCE_LOCATION)
            location?.let {currentMarker->
                viewModel.commerceLocation.value = CommerceLocation().apply {
                    latitude = currentMarker.latitude
                    longitude = currentMarker.longitude
                }
            }
        }
    }
}
