package com.webster.commerces.entity

import com.webster.commerces.BuildConfig
import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Commerce(
    val name: String,
    val commerceId: String,
    val phone: Long,
    val cityId: String,
    val commerceImage: String,
    val address: String,
    val categoryId: String,
    val description: String,
    val images: List<String>,
    val whatsapp: String = Constants.EMPTY_STRING,
    val facebook: String = Constants.EMPTY_STRING
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

    fun urlImage(): String = BuildConfig.BASE_URL + commerceImage
}
