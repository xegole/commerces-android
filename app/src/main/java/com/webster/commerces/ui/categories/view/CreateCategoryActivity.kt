package com.webster.commerces.ui.categories.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.databinding.ActivityCreateCategoryBinding
import com.webster.commerces.entity.Category
import com.webster.commerces.ui.categories.CreateCategoryViewModel
import com.webster.commerces.utils.Constants
import kotlinx.android.synthetic.main.activity_create_category.*


const val RESULT_CODE_GALLERY = 121
const val EXTRA_EDIT_MODE_CATEGORY = "extra_edit_mode_category"
const val EXTRA_CATEGORY_DATA = "extra_category_data"

class CreateCategoryActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateCategoryViewModel::class.java)
    }

    private val editMode by lazy {
        intent.extras?.getBoolean(EXTRA_EDIT_MODE_CATEGORY, false) ?: false
    }

    private val categoryData by lazy {
        intent.extras?.getSerializable(EXTRA_CATEGORY_DATA) as Category?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateCategoryBinding>(this, R.layout.activity_create_category)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initObservers()

        if (editMode) {
            categoryData?.let {
                viewModel.initEditMode(it)
                if (it.categoryImage.isNotEmpty()) {
                    Picasso.get().load(it.categoryImage).into(imageCategory)
                }
                binding.buttonCreate.setText(R.string.label_update_category)
            }
        }
    }

    private fun initObservers() {
        viewModel.openGalleryLiveData.observe(this) {
            startActivityForResult(Intent.createChooser(it, "Seleccione una imagen"), RESULT_CODE_GALLERY)
        }

        viewModel.categoryChangeLiveData.observe(this) { resource ->
            if (resource != 0) {
                Toast.makeText(this, getString(resource), Toast.LENGTH_SHORT).show()
            }
        }
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
                Picasso.get().load(selectedImageUri).into(imageCategory)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}