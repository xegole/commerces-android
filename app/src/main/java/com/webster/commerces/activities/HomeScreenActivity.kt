package com.webster.commerces.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ShareCompat
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
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addFragment
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.fragments.*
import com.webster.commerces.services.EXTRA_DATA_COMMERCE
import com.webster.commerces.ui.cityselector.view.SelectCityDialog
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.ui.login.view.LoginActivity
import com.webster.commerces.utils.ConstantsArray
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.content_toolbar.*

private const val KEY_DEVICES = "devices"
private const val KEY_COMMERCES_ID = "commerceId"

class HomeScreenActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val citiesReference = firebaseDatabase.getReference(FirebaseReferences.CITIES)
    private val commercesReference = firebaseDatabase.getReference(FirebaseReferences.COMMERCES)

    private var currentFragment = 0

    private val prefs by lazy {
        AppCore.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.side_menu_item_category)

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


        intent?.let {
            val commerceId = it.getStringExtra(EXTRA_DATA_COMMERCE) ?: ""
            if (commerceId.isNotEmpty()) {
                showLoading()
                commercesReference.orderByChild(KEY_COMMERCES_ID).equalTo(commerceId)
                    .addListDataListener<Commerce> { list, success ->
                        if (success) {
                            openCommerceDeals(list.first())
                        }
                        dismissLoading()
                    }
            }
        }
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
            R.id.nav_commerces_own -> validateCurrentFragment(item.itemId, CommercesOwnFragment.instance())
            R.id.nav_commerces -> validateCurrentFragment(item.itemId, CommercesFragment.instance())
            R.id.nav_category -> validateCurrentFragment(item.itemId, CategoryFragment.instance())
            R.id.nav_deal -> validateCurrentFragment(item.itemId, CategoryDealsFragment.instance())
            R.id.nav_about -> validateCurrentFragment(item.itemId, AboutFragment.instance())
            R.id.nav_emergency -> validateCurrentFragment(item.itemId, EmergencyFragment.instance())
            R.id.nav_contact_us -> validateCurrentFragment(
                item.itemId,
                ContactUsFragment.instance()
            )
            R.id.nav_change_city -> showSelectCityDialog()
            R.id.nav_log_out -> logOut()
            R.id.nav_share -> shareApp()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        toolbar.title = item.title
        return true
    }

    private fun shareApp() {
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle("Chooser title")
            .setText("http://play.google.com/store/apps/details?id=$packageName")
            .startChooser();
    }

    private fun validateCurrentFragment(currentId: Int, currentFragment: Fragment) {
        if (this.currentFragment != currentId) {
            addFragment(currentFragment)
            this.currentFragment = currentId
        }
    }

    private fun openCommerceDeals(commerce: Commerce) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(bundle, DealsActivity::class.java, false)
    }

    private fun showSelectCityDialog() {
        SelectCityDialog(this) { cityId ->
            cityId?.let { id ->
                if (prefs.cityId != id) {
                    deleteToken(prefs.cityId)
                    prefs.cityId = id
                    registerToken(id)
                }
            }
            defaultItem()
        }.show()
    }

    private fun logOut() {
        deleteToken(prefs.cityId)
        prefs.clear()
        prefs.firstTime = false
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

    private fun deleteToken(cityId: String) {
        if (prefs.cityId.isNotEmpty()) {
            val uid = prefs.user?.uid ?: ""
            if (uid.isNotEmpty()) {
                citiesReference.child(cityId)
                    .child(KEY_DEVICES)
                    .child(uid)
                    .removeValue()
            }
        }
    }

    private fun defaultItem() {
        navView.menu.getItem(ConstantsArray.DEFAULT_ITEM_MENU).isChecked = true
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