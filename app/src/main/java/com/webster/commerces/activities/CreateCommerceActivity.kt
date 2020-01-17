package com.webster.commerces.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.clear
import com.webster.commerces.extensions.content
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_create_category.buttonCreate
import kotlinx.android.synthetic.main.activity_create_category.textDescription
import kotlinx.android.synthetic.main.activity_create_category.textName
import kotlinx.android.synthetic.main.activity_create_category.toolbar
import kotlinx.android.synthetic.main.activity_create_category.viewGallery
import kotlinx.android.synthetic.main.activity_create_commerce.*

class CreateCommerceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var category: Category? = null

    private val database = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)
    private val bannerReference = database.getReference(FirebaseReferences.BANNERS)
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)
    private val myRef = database.getReference(FirebaseReferences.CATEGORIES)

    var imageFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_commerce)
        selected_country.registerPhoneNumberTextView(textWhatsapp)

        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        buttonCreate.setOnClickListener {
            saveCommerce()
        }

        viewGallery.setOnClickListener {
            onGalleryClick()
        }

        myRef.addListDataListener<Category> { list, success ->
            if (success) {
                setSpinnerWithCategories(list)
            }
        }
    }

    //Spinner Category
    private fun setSpinnerWithCategories(listCategories: List<Category>) {
        spinnerCategories!!.onItemSelectedListener = this
        val adapterCategories = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategories)
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories!!.adapter = adapterCategories
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        category = parent?.getItemAtPosition(position) as Category
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do nothing
    }



    private fun saveCommerce() {
        val commerceName = textName.content()
        if (commerceName.isNotEmpty() && imageFile != null) {
            val id = commercesReference.push().key ?: Constants.EMPTY_STRING

            if (id.isNotEmpty()) {
                imageFile?.run {
                    uploadImageCommerce(this, id, commerceName)
                }
            }
        }
    }

    private fun saveCommerceToFirebase(id: String, commerceName: String, commerceImage: String?) {
        val commerce = Commerce()
        commerce.commerceId = id
        commerce.name = commerceName
        commerce.description = textDescription.content()
        commerce.address = textAddress.content()
        commerce.phone = textNumber.content().toLong()
        commerce.whatsapp = textWhatsapp.content()
        commerce.facebook = textFacebook.content()
        commerce.commerceImage = commerceImage ?: Constants.EMPTY_STRING
        commercesReference.child(id).setValue(commerce).addOnSuccessListener {
            textName.clear()
        }
    }

    private fun onGalleryClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), RESULT_CODE_GALLERY)
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
                Picasso.get().load(selectedImageUri).into(imageCommerce)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
