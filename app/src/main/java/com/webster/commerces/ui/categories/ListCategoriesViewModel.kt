package com.webster.commerces.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.FirebaseReferences

class ListCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    val categoryData = MutableLiveData<List<Category>>()

    fun loadListCategories() {
        categoriesReference.addListDataListener<Category> { list, success ->
            categoryData.value = if (success) list else emptyList()
        }
    }
}