package com.webster.commerces.ui.commerces.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.R
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.CommerceLocation
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.extensions.serializeToMap
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
    val commerceCreatedSuccess = MutableLiveData(0)
    val liveDataIntent = MutableLiveData<Intent>()

    val liveDataLoading = MutableLiveData(false)

    var imageFile: Uri? = null
    var category: Category? = null
    var city: City? = null
    private var currentCommerce: Commerce? = null
    private var editMode = false

    val prefs by lazy {
        Prefs(getApplication())
    }

    fun initEditMode(commerce: Commerce) {
        currentCommerce = commerce
        editMode = true

        commerceName.value = commerce.name
        commerceDescription.value = commerce.description
        commerceAddress.value = commerce.address
        commercePhone.value = commerce.phone.toString()
        commerceWhatsapp.value = commerce.whatsapp
        commerceFacebook.value = commerce.facebook
        commerceInstagram.value = commerce.instagram
        commerceWebPage.value = commerce.webPage
        commerceEmail.value = commerce.email
        commerceLocation.value = commerce.location

        val albumFiles = commerce.images.map { imageData ->
            val album = AlbumFile().apply {
                path = imageData
            }
            album
        }
        commercesImages.value = albumFiles
    }

    fun onAddCommerceImages() = View.OnClickListener {
        Album.image(it.context)
            .multipleChoice()
            .camera(true)
            .columnCount(4)
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
        if (editMode) {
            currentCommerce?.let { commerce ->
                updateCommerceToFirebase(commerce)
            }
        } else {
            saveCommerce()
        }
    }

    fun onMapsActivity() = View.OnClickListener {
        showWarning(it.context)
    }

    private fun showWarning(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(R.string.label_location_commerce)
        alertDialog.setMessage(R.string.label_location_message)
        alertDialog.setPositiveButton(R.string.button_text_done) { _, _ ->
            val intent = Intent(context, MapsActivity::class.java)
            liveDataIntent.value = intent
        }
        alertDialog.setNegativeButton(R.string.label_cancel) { _, _ ->

        }
        alertDialog.show()
        alertDialog.create()
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
            commercesReference.child(id).setValue(getCommerce(id, commerceName)).addOnSuccessListener {
                imageFile?.run {
                    uploadBannerImageCommerce(this, id)
                }
                uploadImagesCommerce(id)
                liveDataLoading.value = false
                commerceCreatedSuccess.value = R.string.message_created_commerce_success
            }.addOnFailureListener {
                liveDataLoading.value = false
                commerceCreatedSuccess.value = R.string.message_error_created_commerce_success
            }
        }
    }

    private fun updateCommerceToFirebase(commerce: Commerce) {
        val commerceName = commerceName.value ?: commerce.name
        if (commerceName.isNotEmpty()) {
            liveDataLoading.value = true
            val commerceToUpdate = getCommerce(commerce.commerceId, commerceName)
            commerceToUpdate.verified = commerce.verified
            commerceToUpdate.commerceImage = commerce.commerceImage
            commerceToUpdate.images = commerce.images
            commercesReference.child(commerce.commerceId).updateChildren(commerceToUpdate.serializeToMap()).addOnSuccessListener {
                imageFile?.run {
                    uploadBannerImageCommerce(this, commerce.commerceId)
                }
                uploadImagesCommerce(commerce.commerceId)
                liveDataLoading.value = false
                commerceCreatedSuccess.value = R.string.message_updated_commerce_success
            }.addOnFailureListener {
                liveDataLoading.value = false
                commerceCreatedSuccess.value = R.string.message_error_updated_commerce_success
            }
        }
    }

    private fun getCommerce(commerceId: String, name: String): Commerce {
        val commerce = Commerce()
        commerce.commerceId = commerceId
        commerce.name = name
        commerce.description = commerceDescription.value ?: Constants.EMPTY_STRING
        commerce.cityId = city?.cityId ?: "1"
        commerce.address = commerceAddress.value ?: Constants.EMPTY_STRING
        commerce.location = commerceLocation.value ?: CommerceLocation()
        commerce.categoryId = category?.categoryId ?: Constants.EMPTY_STRING
        commerce.phone = commercePhone.value?.toLong() ?: Constants.LONG_ZERO
        commerce.whatsapp = commerceWhatsapp.value ?: Constants.EMPTY_STRING
        commerce.facebook = commerceFacebook.value ?: Constants.EMPTY_STRING
        commerce.instagram = commerceInstagram.value ?: Constants.EMPTY_STRING
        commerce.webPage = commerceWebPage.value ?: Constants.EMPTY_STRING
        commerce.email = commerceEmail.value ?: Constants.EMPTY_STRING
        commerce.uid = prefs.user?.uid
        return commerce
    }

    private fun updateBannerCommerce(id: String, urlImage: String?) {
        commercesReference.child(id).child("commerceImage").setValue(urlImage)
    }

    private fun updateImagesCommerce(id: String, urlImage: String?, index: Int) {
        commercesReference.child(id).child("images").child("$index").setValue(urlImage)
    }
}