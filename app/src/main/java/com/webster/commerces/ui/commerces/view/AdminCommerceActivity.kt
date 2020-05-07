package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.activities.CreateCommerceActivity
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.ui.commerces.viewmodel.AdminCommerceViewModel
import com.webster.commerces.utils.Prefs
import com.yanzhenjie.album.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_list_commerces.*

class AdminCommerceActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(AdminCommerceViewModel::class.java)
    }

    private val adapter by lazy {
        CommercesAdapter { commerce, _ ->
        }
    }

    private val prefs by lazy {
        Prefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_commerce)
        setSupportActionBar(toolbar)
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
                if (adapter.itemCount == 0) {
                    goToActivity(CreateCommerceActivity::class.java, false)
                }
            }
        }
        return true
    }
}