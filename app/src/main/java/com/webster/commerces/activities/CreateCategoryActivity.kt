package com.webster.commerces.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.clear
import com.webster.commerces.extensions.content
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_create_category.*
import kotlinx.android.synthetic.main.activity_create_category.imageCategory
import kotlinx.android.synthetic.main.category_item_adapter.*


const val RESULT_CODE_GALLERY = 121

class CreateCategoryActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    var imageFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        buttonCreate.setOnClickListener {
            saveCategory()
        }

        viewGallery.setOnClickListener {
            onGalleryClick()
        }
    }

    private fun saveCategory() {
        val categoryName = textName.content()
        if (categoryName.isNotEmpty() && imageFile != null) {
            val id = categoriesReference.push().key ?: Constants.EMPTY_STRING

            if (id.isNotEmpty()) {
                imageFile?.run {
                    uploadImageCategory(this, id, categoryName)
                }
            }
        }
    }

    private fun saveCategoryToFirebase(id: String, categoryName: String, categoryImage: String?) {
        val category = Category()
        category.categoryId = id
        category.name = categoryName
        category.description = textDescription.content()
        category.categoryImage = categoryImage ?: Constants.EMPTY_STRING
        categoriesReference.child(id).setValue(category).addOnSuccessListener {
            textName.clear()
            textDescription.clear()
        }
    }

    private fun onGalleryClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), RESULT_CODE_GALLERY)
    }

    private fun uploadImageCategory(file: Uri, uidCategory: String, categoryName: String) {
        val routeFireStorage = "category/$uidCategory.jpg"
        val storageReference = firebaseStorage.reference.child(routeFireStorage)
        val uploadTask = storageReference.putFile(file)
        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    saveCategoryToFirebase(uidCategory, categoryName, it.toString())
                }
            }
            .addOnProgressListener {
            }
            .addOnFailureListener {
                it.printStackTrace()
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
                imageFile = selectedImageUri
                Picasso.get().load(selectedImageUri).into(imageCategory)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}