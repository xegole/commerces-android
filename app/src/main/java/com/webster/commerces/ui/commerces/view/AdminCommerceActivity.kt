package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.viewmodel.AdminCommerceViewModel
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_admin_commerce.*

class AdminCommerceActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(AdminCommerceViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter { commerce, _ ->
            val extras = Bundle()
            extras.putBoolean(EXTRA_EDIT_MODE, true)
            extras.putSerializable(EXTRA_COMMERCE_DATA, commerce)
            openActivityWithBundle(extras, CreateCommerceActivity::class.java, false)
        }
    }

    private val prefs by lazy {
        Prefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_commerce)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewModel.getCommercesByUuid(prefs.user?.uid)

        initViews()
        initObservers()
    }

    private fun initViews() {
        recyclerCommerces.adapter = adapter
    }

    private fun initObservers() {
        viewModel.commercesListData.observe(this, Observer {
            adapter.addItemList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_admin_commerce, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.createCommerce -> {
                if (adapter.itemCount <= 2) {
                    goToActivity(CreateCommerceActivity::class.java, false)
                }
            }
            android.R.id.home -> finish()
        }
        return true
    }
}