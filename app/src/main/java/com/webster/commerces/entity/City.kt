package com.webster.commerces.entity

data class City(val cityId: String,
                val name: String,
                val longitude: String,
                val latitude: String) {

    override fun toString(): String {
        return name
    }
}