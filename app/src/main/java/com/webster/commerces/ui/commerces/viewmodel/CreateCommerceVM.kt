package com.webster.commerces.ui.commerces.viewmodel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile

class CreateCommerceVM(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)
    private val referenceCategories = database.getReference(FirebaseReferences.CATEGORIES)
    private val referenceCities = database.getReference(FirebaseReferences.CITIES)

    private val albumFiles = ArrayList<AlbumFile>()
    val commercesImages = MutableLiveData<List<AlbumFile>>()
    val listCategories = MutableLiveData<List<Category>>()
    val listCities = MutableLiveData<List<City>>()

    val commerceName = MutableLiveData<String>()
    val commerceDescription = MutableLiveData<String>()

    var imageFile: Uri? = null
    var category: Category? = null
    var city: City? = null

    fun onAddCommerceImages() = View.OnClickListener {
        Album.image(it.context)
            .multipleChoice()
            .camera(true)
            .columnCount(5)
            .selectCount(3)
            .checkedList(albumFiles)
            .filterSize { false }
            .filterMimeType { false }
            .afterFilterVisibility(true)
            .onResult { result ->
                albumFiles.clear()
                albumFiles.addAll(result)
                commercesImages.value = albumFiles
            }
            .onCancel {}
            .start()
    }

    fun onCreateCommerce() = View.OnClickListener {
        saveCommerce()
    }

    fun getCategories() {
        referenceCategories.addListDataListener<Category> { list, success ->
            if (success) {
                listCategories.value = list
            }
        }
    }

    fun getCities() {
        referenceCities.addListDataListener<City> { list, success ->
            if (success) {
                listCities.value = list
            }
        }
    }

    private fun saveCommerce() {
        val commerceName = commerceName.value ?: Constants.EMPTY_STRING
        if (commerceName.isNotEmpty() && imageFile != null) {
            val id = commercesReference.push().key ?: Constants.EMPTY_STRING

            if (id.isNotEmpty()) {
                imageFile?.run {
                    uploadImageCommerce(this, id, commerceName)
                }
            }
        }
    }

    private fun uploadImageCommerce(file: Uri, uidCommerce: String, commerceName: String) {
        val routeFireStorage = "commerces/$uidCommerce.jpg"
        val storageReference = firebaseStorage.reference.child(routeFireStorage)
        val uploadTask = storageReference.putFile(file)
        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    saveCommerceToFirebase(uidCommerce, commerceName, it.toString())
                }
            }
            .addOnProgressListener {
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun saveCommerceToFirebase(id: String, commerceName: String, commerceImage: String?) {
        if (category != null && city != null) {
            val commerce = Commerce()
            commerce.commerceId = category?.categoryId ?: Constants.EMPTY_STRING
            commerce.name = commerceName
            commerce.description = commerceDescription.value ?: Constants.EMPTY_STRING
//            commerce.address = textAddress.content()
//            commerce.phone = textNumber.content().toLong()
//            commerce.whatsapp = textWhatsapp.content()
//            commerce.facebook = textFacebook.content()
            commerce.commerceImage = commerceImage ?: Constants.EMPTY_STRING
//            commercesReference.child(id).setValue(commerce).addOnSuccessListener {
//
//            }
        }
    }
}