package com.webster.commerces.ui.login.model

import android.util.Patterns
import io.fabric.sdk.android.services.common.CommonUtils

class UserLoginData(val email: String, val password: String) {
    private fun isValidEmail() =
        !CommonUtils.isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword() = !CommonUtils.isNullOrEmpty(password) && email.length > 5

    fun validateUser(): UserLogin {
        return if (!isValidEmail()) {
            UserLogin.ERROR_EMAIL
        } else if (!isValidPassword()) {
            UserLogin.ERROR_PASSWORD
        } else {
            UserLogin.VALID_USER
        }
    }
}

enum class UserLogin {
    VALID_USER, ERROR_NOT_FOUND, ERROR_WRONG_PASSWORD, ERROR_EMAIL, ERROR_PASSWORD, ERROR_TOO_MANY_REQUESTS
}