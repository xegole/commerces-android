package com.webster.commerces.entity

data class User(
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var typeUser: TypeUser? = TypeUser.GUEST
)

enum class TypeUser {
    ADMIN, USER_COMMERCE, GUEST
}