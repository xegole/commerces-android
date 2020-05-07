package com.webster.commerces.ui.register.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.AppCore
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.entity.User
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.ui.cityselector.CitySelectorActivity
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

    val liveDataEmail = MutableLiveData<String>()
    val liveDataPassword = MutableLiveData<String>()
    val liveDataValidatePassword = MutableLiveData<String>()
    val liveDataLoading = MutableLiveData(false)
    val liveDataRegisterSuccess = MutableLiveData<Class<*>>()

    fun onLoginActivityClick() = View.OnClickListener {
        it.goActivity(LoginActivity::class.java, true)
    }

    fun onRegisterClick() = View.OnClickListener {
        val userRegister = UserRegister(
            liveDataEmail.value ?: "",
            liveDataPassword.value ?: "",
            liveDataValidatePassword.value ?: ""
        )

        when (userRegister.validateUser()) {
            ValidateUser.ERROR_EMAIL -> {
            }
            ValidateUser.ERROR_PASSWORD -> {
            }
            ValidateUser.ERROR_VALIDATE_PASSWORD -> {
            }
            ValidateUser.ERROR_VERIFY_PASSWORD -> {

            }
            ValidateUser.VALID_USER -> {
                registerInFirebase(userRegister)
            }
        }
    }

    private fun registerInFirebase(userRegister: UserRegister) {
        liveDataLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(userRegister.email, userRegister.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val uid = firebaseUser?.uid ?: ""
                    val user = User(uid, "", userRegister.email, TypeUser.USER_COMMERCE)
                    databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        prefs.user = user
                        liveDataRegisterSuccess.value = CitySelectorActivity::class.java
                    }
                } else {
                    task.exception?.let {
                    }
                }
            }
    }
}