package com.webster.commerces.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.RemoteMessage
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.addFragment
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.fragments.AboutFragment
import com.webster.commerces.fragments.CategoryFragment
import com.webster.commerces.fragments.CommercesFragment
import com.webster.commerces.fragments.ContactUsFragment
import com.webster.commerces.ui.cityselector.CitySelectorActivity
import com.webster.commerces.utils.ConstantsArray
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.content_toolbar.*

class HomeScreenActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentFragment: Int = 0

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
        navView.menu.getItem(ConstantsArray.FIRST).isChecked = true
        onNavigationItemSelected(navView.menu.getItem(ConstantsArray.FIRST))

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        toolbar.title = item.title
        when (item.itemId) {
            R.id.nav_commerces -> validateCurrentFragment(item.itemId, CommercesFragment.instance())
            R.id.nav_category -> validateCurrentFragment(item.itemId, CategoryFragment.instance())
            R.id.nav_about -> validateCurrentFragment(item.itemId, AboutFragment.instance())
            R.id.nav_contact_us -> validateCurrentFragment(item.itemId,ContactUsFragment.instance())
            R.id.nav_exit -> consume { goToActivity(CitySelectorActivity::class.java)}
            R.id.nav_share -> {
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun validateCurrentFragment(currentId: Int, currentFragment: Fragment) {
        if (this.currentFragment != currentId) {
            addFragment(currentFragment)
            this.currentFragment = currentId
        }
    }

    private inline fun consume(f: () -> Unit): Boolean {
        val mSharedPreferences: SharedPreferences = getSharedPreferences(Prefs.Values.PREFS_FILENAME, Context.MODE_PRIVATE)
        val mEditor: SharedPreferences.Editor = mSharedPreferences.edit()
        mEditor.clear()
        mEditor.apply()
        finish()
        f()
        return true
    }
}

