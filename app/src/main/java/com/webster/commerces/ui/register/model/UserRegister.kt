package com.webster.commerces.ui.register.model

import android.util.Patterns
import com.google.firebase.crashlytics.internal.common.CommonUtils.isNullOrEmpty

data class UserRegister(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val validatePassword: String
) {
    private val invalidName = name.isBlank() || name.length < 3

    private fun isValidEmail() =
        !isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword() = !isNullOrEmpty(password) && email.length > 5
    private fun isValidValidatePassword() =
        !isNullOrEmpty(validatePassword) && validatePassword.length > 5

    private fun invalidPhone() = phone.length <= 9
    private fun isSamePassword() = password == validatePassword

    fun validateUser(): ValidateUser {
        return when {
            invalidName -> ValidateUser.ERROR_NAME
            !isValidEmail() -> ValidateUser.ERROR_EMAIL
            invalidPhone() -> ValidateUser.ERROR_PHONE
            !isValidPassword() -> ValidateUser.ERROR_PASSWORD
            !isValidValidatePassword() -> ValidateUser.ERROR_VALIDATE_PASSWORD
            !isSamePassword() -> ValidateUser.ERROR_VERIFY_PASSWORD
            else -> ValidateUser.VALID_USER
        }
    }
}

enum class ValidateUser {
    ERROR_NAME,
    ERROR_EMAIL,
    ERROR_PHONE,
    ERROR_PASSWORD,
    ERROR_VALIDATE_PASSWORD,
    ERROR_VERIFY_PASSWORD,
    VALID_USER,
    ERROR_EMAIL_USE
}

