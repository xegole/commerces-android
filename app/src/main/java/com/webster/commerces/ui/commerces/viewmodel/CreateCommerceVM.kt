package com.webster.commerces.ui.commerces.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.CommerceLocation
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.ui.maps.MapsActivity
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import com.webster.commerces.utils.Prefs
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
    val commerceLocation = MutableLiveData<CommerceLocation>()
    val commercePhone = MutableLiveData<String>()
    val commerceWhatsapp = MutableLiveData<String>()
    val commerceFacebook = MutableLiveData<String>()
    val commerceInstagram = MutableLiveData<String>()
    val commerceWebPage = MutableLiveData<String>()
    val commerceEmail = MutableLiveData<String>()
    val commerceCreatedSuccess = MutableLiveData<Boolean>()
    val liveDataIntent = MutableLiveData<Intent>()

    val liveDataLoading = MutableLiveData(false)

    var imageFile: Uri? = null
    var category: Category? = null
    var city: City? = null

    val prefs by lazy {
        Prefs(getApplication())
    }

    fun initEditMode(commerce: Commerce) {
        commerceName.value = commerce.name
        commerceDescription.value = commerce.description
        commerceAddress.value = commerce.address
        commercePhone.value = commerce.phone.toString()
        commerceWhatsapp.value = commerce.whatsapp
        commerceFacebook.value = commerce.facebook
    }

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
        it.hideKeyboard()
        saveCommerce()
    }

    fun onMapsActivity() = View.OnClickListener {
        val intent = Intent(it.context, MapsActivity::class.java)
        liveDataIntent.value = intent
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
            liveDataLoading.value = true
            val commerce = Commerce()
            commerce.commerceId = id
            commerce.name = commerceName
            commerce.description = commerceDescription.value ?: Constants.EMPTY_STRING
            commerce.cityId = city?.cityId ?: "1"
            commerce.address = commerceAddress.value ?: Constants.EMPTY_STRING
            commerce.location = commerceLocation.value ?: CommerceLocation(0.0, 0.0)
            commerce.categoryId = category?.categoryId ?: Constants.EMPTY_STRING
            commerce.phone = commercePhone.value?.toLong() ?: Constants.LONG_ZERO
            commerce.whatsapp = commerceWhatsapp.value ?: Constants.EMPTY_STRING
            commerce.facebook = commerceFacebook.value ?: Constants.EMPTY_STRING
            commerce.instagram = commerceInstagram.value ?: Constants.EMPTY_STRING
            commerce.webPage = commerceWebPage.value ?: Constants.EMPTY_STRING
            commerce.email = commerceEmail.value ?: Constants.EMPTY_STRING
            commerce.uid = prefs.user?.uid
            commercesReference.child(id).setValue(commerce).addOnSuccessListener {
                imageFile?.run {
                    uploadBannerImageCommerce(this, id)
                }
                uploadImagesCommerce(id)
                liveDataLoading.value = false
                commerceCreatedSuccess.value = true
            }.addOnFailureListener {
                liveDataLoading.value = false
                commerceCreatedSuccess.value = false
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