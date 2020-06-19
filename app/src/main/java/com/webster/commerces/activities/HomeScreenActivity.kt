package com.webster.commerces.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.addFragment
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.fragments.*
import com.webster.commerces.ui.cityselector.view.SelectCityDialog
import com.webster.commerces.ui.login.view.LoginActivity
import com.webster.commerces.utils.ConstantsArray
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.content_toolbar.*

class HomeScreenActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val citiesReference = firebaseDatabase.getReference(FirebaseReferences.CITIES)

    private var currentFragment = 0

    private val prefs by lazy {
        AppCore.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        if (prefs.cityId.isEmpty()) {
            showSelectCityDialog()
        }
        defaultItem()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_commerces -> validateCurrentFragment(item.itemId, CommercesFragment.instance())
            R.id.nav_category -> validateCurrentFragment(item.itemId, CategoryFragment.instance())
            R.id.nav_deal -> validateCurrentFragment(item.itemId, CategoryDealsFragment.instance())
            R.id.nav_about -> validateCurrentFragment(item.itemId, AboutFragment.instance())
            R.id.nav_emergency -> validateCurrentFragment(item.itemId, EmergencyFragment.instance())
            R.id.nav_interesting_places -> validateCurrentFragment(
                item.itemId,
                InterestPlacesFragment.instance()
            )
            R.id.nav_contact_us -> validateCurrentFragment(
                item.itemId,
                ContactUsFragment.instance()
            )
            R.id.nav_change_city -> showSelectCityDialog()
            R.id.nav_log_out -> logOut()
            R.id.nav_share -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        toolbar.title = item.title
        return true
    }

    private fun validateCurrentFragment(currentId: Int, currentFragment: Fragment) {
        if (this.currentFragment != currentId) {
            addFragment(currentFragment)
            this.currentFragment = currentId
        }
    }

    private fun showSelectCityDialog() {
        SelectCityDialog(this) { cityId ->
            cityId?.let { id ->
                if (prefs.cityId != id) {
                    prefs.cityId = id
                    registerToken(id)
                }
            }
            defaultItem()
        }.show()
    }

    private fun logOut() {
        prefs.clear()
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInClient?.signOut()
        firebaseAuth.signOut()
        goToActivity(LoginActivity::class.java)
    }

    private fun defaultItem() {
        navView.menu.getItem(ConstantsArray.SECOND).isChecked = true
        onNavigationItemSelected(navView.menu.findItem(R.id.nav_category))
        toolbar.title = getString(R.string.side_menu_item_category)
    }

    private fun registerToken(cityId: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val tokenDevice = task.result?.token ?: ""
                updateToken(tokenDevice, cityId)
            })
    }

    private fun updateToken(token: String, cityId: String) {
        prefs.tokenDevice = token
        citiesReference.child(cityId)
            .child("devices")
            .child(prefs.user?.uid ?: "")
            .setValue(token)
            .addOnSuccessListener {
            }
    }
}