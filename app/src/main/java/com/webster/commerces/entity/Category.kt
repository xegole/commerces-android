package com.webster.commerces.entity

import com.webster.commerces.BuildConfig
import com.webster.commerces.services.RetrofitServices
import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Category(
    val categoryId: String,
    val name: String,
    val categoryImage: String,
    val description: String
) : Serializable {

    constructor() : this(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING)

    fun urlImage(): String = BuildConfig.BASE_URL + categoryImage
}