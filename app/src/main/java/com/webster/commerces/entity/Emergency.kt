package com.webster.commerces.entity

import com.webster.commerces.utils.Constants

data class Emergency(
    val type: Int,
    val number: String,
    val whatsapp: String,
    val dialNumber: Int = 1,
    val cityId: String
) {
    constructor() : this(
        Constants.INT_ZERO,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.INT_ZERO,
        Constants.EMPTY_STRING
    )
}