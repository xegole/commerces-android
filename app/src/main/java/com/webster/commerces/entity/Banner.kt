package com.webster.commerces.entity

import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Banner(
    val bannerImage: String,
    val name: String,
    val description: String,
    val bannerId: String
) : Serializable {
    constructor() : this(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING)
}