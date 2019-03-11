package com.webster.commerces.entity

import com.webster.commerces.services.RetrofitServices.BASE_URL
import java.io.Serializable

data class Commerce(
    val name: String,
    val commerceId: String,
    val phone: Int,
    val cityId: String,
    val commerceImage: String,
    val address: String
) : Serializable {

    fun urlImage(): String = BASE_URL + commerceImage
}
