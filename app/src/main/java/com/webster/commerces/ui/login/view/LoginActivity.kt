package com.webster.commerces.ui.login.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.databinding.ActivityLoginBinding
import com.webster.commerces.entity.TypeUser
import com.webster.commerces.entity.User
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.cityselector.CitySelectorActivity
import com.webster.commerces.ui.login.viewmodel.LoginViewModel
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_login.*


const val RC_SIGN_IN = 203
const val USERS_DATABASE = "users"

class LoginActivity : BaseActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = firebaseDatabase.reference.child(USERS_DATABASE)
    private val prefs by lazy {
        Prefs(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initGoogleSignInClient()
        buttonSignInGoogle.setOnClickListener {
            initSignIn()
        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun initSignIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(ApiException::class.java)
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
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
                    val isNewUser = authTask.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val user = User(uid, name ?: "", email ?: "", TypeUser.USER)
                        if (isNewUser == true) {
                            databaseReference.child(uid).setValue(user).addOnCompleteListener {
                                prefs.typeUser = TypeUser.USER
                                goToActivity(CitySelectorActivity::class.java)
                            }
                        } else {
                            databaseReference.child(uid)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val userData = dataSnapshot.getValue(User::class.java)
                                        prefs.typeUser = userData?.typeUser ?: TypeUser.USER
                                        goToActivity(CitySelectorActivity::class.java)
                                    }

                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                        }


                    }
                } else {
                    authTask.exception?.printStackTrace()
                }
            }
    }

    private fun signOutGoogle() {
        googleSignInClient?.signOut()
    }
}