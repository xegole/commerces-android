package com.webster.commerces.entity

data class User(
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var typeUser: TypeUser? = TypeUser.USER
)

enum class TypeUser {
    ADMIN, USER_COMMERCE, USER
}