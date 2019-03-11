package com.webster.commerces.entity

import com.webster.commerces.services.RetrofitServices
import java.io.Serializable

data class Category(
    val categoryId: String,
    val name: String,
    val categoryImage: String,
    val description: String
) : Serializable {

    fun urlImage(): String = RetrofitServices.BASE_URL + categoryImage
}