package com.webster.commerces.entity

import com.webster.commerces.utils.Constants

data class City(
    val cityId: String,
    var name: String,
    val longitude: String,
    val latitude: String
) {

    constructor() : this(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING)

    override fun toString(): String {
        return name
    }
}