package com.webster.commerces.ui.register.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.AppCore
import com.webster.commerces.activities.HomeScreenActivity
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.entity.User
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.ui.login.view.LoginActivity
import com.webster.commerces.ui.login.view.USERS_DATABASE
import com.webster.commerces.ui.register.model.UserRegister
import com.webster.commerces.ui.register.model.ValidateUser

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = firebaseDatabase.reference.child(USERS_DATABASE)
    private val prefs by lazy {
        AppCore.prefs
    }

    val nameLiveData = MutableLiveData<String>()
    val liveDataEmail = MutableLiveData<String>()
    val liveDataPhone = MutableLiveData<String>()
    val liveDataPassword = MutableLiveData<String>()
    val liveDataValidatePassword = MutableLiveData<String>()
    val liveDataLoading = MutableLiveData(false)
    val liveDataVerifyEmail = MutableLiveData(false)
    val liveDataRegisterSuccess = MutableLiveData<Class<*>>()

    val liveDataError = MutableLiveData<ValidateUser>()

    fun onLoginActivityClick() = View.OnClickListener {
        it.goActivity(LoginActivity::class.java, true)
    }

    fun onRegisterClick() = View.OnClickListener {
        it.hideKeyboard()
        val userRegister = UserRegister(
            nameLiveData.value ?: "",
            liveDataEmail.value ?: "",
            liveDataPhone.value ?: "",
            liveDataPassword.value ?: "",
            liveDataValidatePassword.value ?: ""
        )
        val validateUser = userRegister.validateUser()
        liveDataError.value = validateUser
        if (validateUser == ValidateUser.VALID_USER) {
            registerInFirebase(userRegister)
        }
    }

    private fun registerInFirebase(userRegister: UserRegister) {
        liveDataLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(userRegister.email, userRegister.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser?.isEmailVerified == true) {
                        val uid = firebaseUser.uid
                        val user = User(uid, "", userRegister.email, TypeUser.USER_COMMERCE)
                        databaseReference.child(uid).setValue(user).addOnCompleteListener {
                            prefs.user = user
                            liveDataRegisterSuccess.value = HomeScreenActivity::class.java
                        }
                    } else {
                        firebaseUser?.sendEmailVerification()
                            ?.addOnCompleteListener {
                            }
                        liveDataVerifyEmail.value = true
                        liveDataRegisterSuccess.value = LoginActivity::class.java
                    }
                } else {
                    task.exception?.let {
                        if (it is FirebaseAuthUserCollisionException) {
                            if (it.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                                liveDataError.value = ValidateUser.ERROR_EMAIL_USE
                            }
                        }
                    }
                }
                liveDataLoading.value = false
            }.addOnFailureListener {
                it.printStackTrace()
                liveDataLoading.value = false
            }
    }
}