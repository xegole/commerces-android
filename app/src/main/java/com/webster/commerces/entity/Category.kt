package com.webster.commerces.entity

import com.webster.commerces.utils.Constants
import java.io.Serializable

data class Category(
    var categoryId: String = Constants.EMPTY_STRING,
    var name: String = Constants.EMPTY_STRING,
    var description: String = Constants.EMPTY_STRING,
    var categoryImage: String = Constants.EMPTY_STRING
) : Serializable {
    override fun toString(): String {
        return name
    }
}