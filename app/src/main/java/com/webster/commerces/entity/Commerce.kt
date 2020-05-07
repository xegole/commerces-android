package com.webster.commerces.entity

import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Commerce(
    var name: String,
    var commerceId: String,
    var phone: Long,
    var cityId: String,
    var commerceImage: String,
    var address: String,
    var categoryId: String,
    var description: String,
    val images: List<String>,
    var whatsapp: String = Constants.EMPTY_STRING,
    var facebook: String = Constants.EMPTY_STRING,
    var instagram: String = Constants.EMPTY_STRING,
    var uid: String? = Constants.EMPTY_STRING
) : Serializable {

    constructor() : this(
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.LONG_ZERO,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        emptyList()
    )

    fun urlImage(): String = commerceImage
}
