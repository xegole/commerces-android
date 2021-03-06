package com.webster.commerces.activities

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.addFragment
import com.webster.commerces.fragments.CategoryFragment
import com.webster.commerces.fragments.CommercesFragment
import com.webster.commerces.utils.ConstantsArray
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.content_toolbar.*

class HomeScreenActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
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
            R.id.nav_commerces -> {
                val currentFragment = CommercesFragment.newInstance()
                validateCurrentFragment(item.itemId, currentFragment)
            }
            R.id.nav_category -> {
                val currentFragment = CategoryFragment.newInstance()
                validateCurrentFragment(item.itemId, currentFragment)
            }
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
}
