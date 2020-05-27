package com.webster.commerces.entity

import com.webster.commerces.utils.Constants
import java.io.Serializable

class Commerce : Serializable {

    var name = Constants.EMPTY_STRING
    var commerceId = Constants.EMPTY_STRING
    var phone = Constants.LONG_ZERO
    var cityId = Constants.EMPTY_STRING
    var commerceImage = Constants.EMPTY_STRING
    var address = Constants.EMPTY_STRING
    var location = CommerceLocation(0.0, 0.0)
    var categoryId = Constants.EMPTY_STRING
    var description = Constants.EMPTY_STRING
    val images = listOf<String>()
    var whatsapp: String = Constants.EMPTY_STRING
    var facebook: String = Constants.EMPTY_STRING
    var instagram: String = Constants.EMPTY_STRING
    var webPage: String = Constants.EMPTY_STRING
    var email: String = Constants.EMPTY_STRING
    var uid: String? = Constants.EMPTY_STRING
    var comments: HashMap<String, Comment> = hashMapOf()

    fun urlImage(): String = commerceImage
}
