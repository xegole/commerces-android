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
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
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
import com.webster.commerces.extensions.hideKeyboard
import com.webster.commerces.ui.cityselector.view.CitySelectorActivity
import com.webster.commerces.ui.login.model.UserLogin
import com.webster.commerces.ui.login.model.UserLoginData
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
    val liveDataError = MutableLiveData<UserLogin>()

    fun onAppLoginClick() = View.OnClickListener {
        liveDataLoading.value = true
        liveDataError.value = UserLogin.VALID_USER
        it.hideKeyboard()
        onLogin()
    }

    fun initTestData() {
        liveDataEmail.value = "xegole@hotmail.com"
        liveDataPassword.value = "diegoleon89"
    }

    fun onRegisterClick() = View.OnClickListener {
        it.goActivity(RegisterActivity::class.java, true)
    }

    fun onGuestClick() = View.OnClickListener {
        prefs.clear()
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
            if (firebaseUser.isEmailVerified) {
                val uid = firebaseUser.uid
                val name = firebaseUser.displayName
                val email = firebaseUser.email
                val user = User(uid, name ?: "", email ?: "", TypeUser.USER_COMMERCE)
                if (isNewUser == true) {
                    databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        prefs.user = user
                        registerSuccess.value = CitySelectorActivity::class.java
                        liveDataLoading.value = false
                    }
                } else {
                    databaseReference.child(uid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val userData = dataSnapshot.getValue(User::class.java)
                                prefs.user = userData
                                registerSuccess.value = CitySelectorActivity::class.java
                                liveDataLoading.value = false
                            }

                            override fun onCancelled(p0: DatabaseError) {
                                liveDataLoading.value = false
                            }
                        })
                }
            } else {
                firebaseUser.sendEmailVerification()
                    .addOnCompleteListener {
                    }
                liveDataLoading.value = false
                liveDataError.value = UserLogin.EMAIL_NOT_VERIFIED
            }
        }
    }

    private fun onLogin() {
        val userLogin = UserLoginData(liveDataEmail.value ?: "", liveDataPassword.value ?: "")
        val validateUser = userLogin.validateUser()
        if (validateUser == UserLogin.VALID_USER) {
            firebaseAuth.signInWithEmailAndPassword(userLogin.email, userLogin.password)
                .addOnCompleteListener { authTask: Task<AuthResult> ->
                    if (authTask.isSuccessful) {
                        loginRegisterFirebase(authTask)
                    } else {
                        authTask.exception?.let {
                            if (it is FirebaseAuthInvalidUserException) {
                                if (it.errorCode == "ERROR_USER_NOT_FOUND") {
                                    liveDataError.value = UserLogin.ERROR_NOT_FOUND
                                }
                            } else if (it is FirebaseAuthInvalidCredentialsException) {
                                if (it.errorCode == "ERROR_WRONG_PASSWORD") {
                                    liveDataError.value = UserLogin.ERROR_WRONG_PASSWORD
                                }
                            } else if (it is FirebaseTooManyRequestsException) {
                                liveDataError.value = UserLogin.ERROR_TOO_MANY_REQUESTS
                            }
                        }
                    }
                }.addOnFailureListener {
                    liveDataLoading.value = false
                }
        } else {
            liveDataLoading.value = false
            liveDataError.value = validateUser
        }
    }
}