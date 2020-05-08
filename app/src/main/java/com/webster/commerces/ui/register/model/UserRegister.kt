package com.webster.commerces.ui.register.model

import android.util.Patterns
import io.fabric.sdk.android.services.common.CommonUtils.isNullOrEmpty

data class UserRegister(
    val email: String,
    val phone: String,
    val password: String,
    val validatePassword: String
) {

    private fun isValidEmail() =
        !isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword() = !isNullOrEmpty(password) && email.length > 5
    private fun isValidValidatePassword() =
        !isNullOrEmpty(validatePassword) && validatePassword.length > 5

    private fun invalidPhone() = phone.length <= 9
    private fun isSamePassword() = password == validatePassword

    fun validateUser(): ValidateUser {
        return if (!isValidEmail()) {
            ValidateUser.ERROR_EMAIL
        } else if (invalidPhone()) {
            ValidateUser.ERROR_PHONE
        } else if (!isValidPassword()) {
            ValidateUser.ERROR_PASSWORD
        } else if (!isValidValidatePassword()) {
            ValidateUser.ERROR_VALIDATE_PASSWORD
        } else if (!isSamePassword()) {
            ValidateUser.ERROR_VERIFY_PASSWORD
        } else {
            ValidateUser.VALID_USER
        }
    }
}

enum class ValidateUser {
    ERROR_EMAIL,
    ERROR_PHONE,
    ERROR_PASSWORD,
    ERROR_VALIDATE_PASSWORD,
    ERROR_VERIFY_PASSWORD,
    VALID_USER,
    ERROR_EMAIL_USE
}

