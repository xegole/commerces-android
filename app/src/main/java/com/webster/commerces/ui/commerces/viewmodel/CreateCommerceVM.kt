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
import java.io.File

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
    val commerceAddress = MutableLiveData<String>()

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
            saveCommerceToFirebase(id, commerceName)
        }
    }

    private fun uploadBannerImageCommerce(file: Uri, uidCommerce: String) {
        val routeFireStorage = "commerces/$uidCommerce.jpg"
        val storageReference = firebaseStorage.reference.child(routeFireStorage)
        val uploadTask = storageReference.putFile(file)
        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    updateBannerCommerce(uidCommerce, it.toString())
                }
            }
            .addOnProgressListener {
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun uploadImagesCommerce(uidCommerce: String) {
        if (albumFiles.isNotEmpty()) {
            albumFiles.forEachIndexed { index, albumFile ->
                val routeFireStorage = "commerces/$uidCommerce/$index"
                val storageReference = firebaseStorage.reference.child(routeFireStorage)
                val image = File(albumFile.path)
                val uploadTask = storageReference.putFile(Uri.fromFile(image))
                uploadTask
                    .addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            updateImagesCommerce(uidCommerce, it.toString(), index)
                        }
                    }
                    .addOnProgressListener {
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        }
    }

    private fun saveCommerceToFirebase(id: String, commerceName: String) {
        if (category != null && city != null) {
            val commerce = Commerce()
            commerce.commerceId = category?.categoryId ?: Constants.EMPTY_STRING
            commerce.name = commerceName
            commerce.description = commerceDescription.value ?: Constants.EMPTY_STRING
            commerce.cityId = city?.cityId ?: "1"
            commerce.address = commerceAddress.value ?: Constants.EMPTY_STRING
            commerce.categoryId = category?.categoryId ?: Constants.EMPTY_STRING
//            commerce.phone = textNumber.content().toLong()
//            commerce.whatsapp = textWhatsapp.content()
//            commerce.facebook = textFacebook.content()
            commercesReference.child(id).setValue(commerce).addOnSuccessListener {
                imageFile?.run {
                    uploadBannerImageCommerce(this, id)
                }

                uploadImagesCommerce(id)
            }
        }
    }

    private fun updateBannerCommerce(id: String, urlImage: String?) {
        commercesReference.child(id).child("commerceImage").setValue(urlImage)
    }

    private fun updateImagesCommerce(id: String, urlImage: String?, index: Int) {
        commercesReference.child(id).child("images").child("$index").setValue(urlImage)
    }
}