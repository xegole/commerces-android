package com.webster.commerces.ui.login.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.entity.User
import com.webster.commerces.extensions.getString
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.ui.cityselector.CitySelectorActivity
import com.webster.commerces.ui.commerces.view.AdminCommerceActivity
import com.webster.commerces.ui.login.view.RC_SIGN_IN
import com.webster.commerces.ui.login.view.USERS_DATABASE
import com.webster.commerces.ui.register.view.RegisterActivity

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = firebaseDatabase.reference.child(USERS_DATABASE)
    private val prefs by lazy {
        AppCore.prefs
    }

    val liveDataEmail = MutableLiveData<String>()
    val liveDataPassword = MutableLiveData<String>()
    val registerSuccess = MutableLiveData<Class<*>>()
    val liveDataLoading = MutableLiveData(false)

    fun onAppLoginClick() = View.OnClickListener {
        liveDataLoading.value = true
        onLogin()
    }

    fun onRegisterClick() = View.OnClickListener {
        it.goActivity(RegisterActivity::class.java, true)
    }

    fun onGuestClick() = View.OnClickListener {
        registerSuccess.value = CitySelectorActivity::class.java
    }

    fun onGoogleLoginClick() = View.OnClickListener {
        liveDataLoading.value = true
        val signInIntent = googleSignInClient?.signInIntent
        (it.context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun initGoogleSignInClient(context: AppCompatActivity) {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    }

    fun onActivityResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val googleSignInAccount = task.getResult(ApiException::class.java)
            googleSignInAccount?.let { getGoogleAuthCredential(it) }
        } catch (e: ApiException) {
            e.printStackTrace()
            liveDataLoading.value = false
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        firebaseAuth.signInWithCredential(googleAuthCredential)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    loginRegisterFirebase(authTask)
                } else {
                    authTask.exception?.printStackTrace()
                }
                liveDataLoading.value = false
            }.addOnFailureListener {
                liveDataLoading.value = false
            }
    }

    private fun loginRegisterFirebase(authTask: Task<AuthResult>) {
        val isNewUser = authTask.result?.additionalUserInfo?.isNewUser
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val uid = firebaseUser.uid
            val name = firebaseUser.displayName
            val email = firebaseUser.email
            val user = User(uid, name ?: "", email ?: "", TypeUser.USER_COMMERCE)
            if (isNewUser == true) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    prefs.user = user
                    registerSuccess.value = CitySelectorActivity::class.java
                }
            } else {
                databaseReference.child(uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val userData = dataSnapshot.getValue(User::class.java)
                            prefs.user = userData
                            when (userData?.typeUser) {
                                TypeUser.ADMIN -> registerSuccess.value =
                                    AdminCommerceActivity::class.java
                                else -> registerSuccess.value = CitySelectorActivity::class.java
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })
            }
        }
    }

    private fun onLogin() {
        val email = liveDataEmail.value ?: ""
        val password = liveDataPassword.value ?: ""
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { authTask: Task<AuthResult> ->
                    if (authTask.isSuccessful) {
                        loginRegisterFirebase(authTask)
                    } else {
                        authTask.exception?.printStackTrace()
                    }
                    liveDataLoading.value = false
                }.addOnFailureListener {
                    liveDataLoading.value = false
                }
        }
    }
}