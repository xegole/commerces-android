package com.webster.commerces.ui.categories

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.R
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.serializeToMap
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

class CreateCategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    val openGalleryLiveData = MutableLiveData<Intent>()
    val categoryNameLiveData = MutableLiveData<String>()
    val categoryDescriptionLiveData = MutableLiveData<String>()
    val categoryChangeLiveData = MutableLiveData(0)

    var imageFile: Uri? = null
    private var editMode = false
    private var currentCategory: Category? = null

    fun initEditMode(category: Category) {
        currentCategory = category
        editMode = true
        categoryNameLiveData.value = category.name
        categoryDescriptionLiveData.value = category.description
    }

    val onClickUploadImage = View.OnClickListener {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        openGalleryLiveData.value = intent
    }

    val saveOrUpdateCategory = View.OnClickListener {
        val categoryName = categoryNameLiveData.value ?: ""
        if (categoryName.isNotEmpty()) {
            if (editMode) {
                currentCategory?.let {
                    updateCategoryToFirebase(it)
                }
            } else {
                val id = categoriesReference.push().key ?: Constants.EMPTY_STRING
                if (id.isNotEmpty()) {
                    imageFile?.run {
                        uploadImageCategory(false, this, id, categoryName)
                    }
                }
            }
        }
    }

    private fun saveCategoryToFirebase(id: String, categoryName: String, categoryImage: String?) {
        val category = Category()
        category.categoryId = id
        category.name = categoryName
        category.description = categoryDescriptionLiveData.value ?: ""
        category.categoryImage = categoryImage ?: Constants.EMPTY_STRING
        categoriesReference.child(id).setValue(category).addOnSuccessListener {
            categoryNameLiveData.value = ""
            categoryDescriptionLiveData.value = ""
            categoryChangeLiveData.value = R.string.message_created_category_success
        }.addOnFailureListener {
            categoryNameLiveData.value = ""
            categoryDescriptionLiveData.value = ""
            categoryChangeLiveData.value = R.string.message_error_created_category
        }
    }

    private fun updateCategoryToFirebase(data: Category) {
        val categoryName = categoryNameLiveData.value ?: data.name
        if (categoryName.isNotEmpty()) {
            val category = Category()
            category.categoryId = data.categoryId
            category.name = categoryName
            category.description = categoryDescriptionLiveData.value ?: ""
            category.categoryImage = data.categoryImage
            categoriesReference.child(data.categoryId).updateChildren(category.serializeToMap()).addOnSuccessListener {
                categoryChangeLiveData.value = R.string.message_updated_category_success
                imageFile?.run {
                    uploadImageCategory(true, this, data.categoryId)
                }
            }.addOnFailureListener {
                categoryChangeLiveData.value = R.string.message_error_update_category
            }
        }
    }

    private fun uploadImageCategory(update: Boolean = false, file: Uri, uidCategory: String, categoryName: String = "") {
        val routeFireStorage = "category/$uidCategory.jpg"
        val storageReference = firebaseStorage.reference.child(routeFireStorage)
        val uploadTask = storageReference.putFile(file)
        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    if (update) {
                        updateCategoryImage(uidCategory, it.toString())
                    } else {
                        saveCategoryToFirebase(uidCategory, categoryName, it.toString())
                    }
                }
            }
            .addOnProgressListener {
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun updateCategoryImage(id: String, urlImage: String?) {
        categoriesReference.child(id).child("categoryImage").setValue(urlImage)
    }
}