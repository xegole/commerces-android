package com.webster.commerces.entity

import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Emergency(
    val type: Int,
    val number: String,
    val whatsapp: String,
    val description: String,
    val cityId: String,
    var id: String
) : Serializable {
    constructor() : this(
        Constants.INT_ZERO,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING
    )
}