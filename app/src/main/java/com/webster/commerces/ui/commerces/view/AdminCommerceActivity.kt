package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.viewmodel.AdminCommerceViewModel
import com.webster.commerces.ui.deal.DealCommerceActivity
import com.webster.commerces.ui.deal.EXTRA_COMMERCE_ID
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_admin_commerce.*

class AdminCommerceActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(AdminCommerceViewModel::class.java)
    }

    private var editCommerceDialog: EditCommerceDialog? = null

    private val adapter by lazy {
        CommercesAdapter { commerce, _ ->
            editCommerceDialog = EditCommerceDialog(this) {
                when (it) {
                    EditState.EDIT -> goEditCommerce(commerce)
                    EditState.DELETE -> deleteDialog(commerce.commerceId)
                    EditState.DEAL -> goCommerceDeals(commerce.commerceId)
                }
            }
            editCommerceDialog?.show()
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

    private fun goEditCommerce(commerce: Commerce) {
        val extras = Bundle()
        extras.putBoolean(EXTRA_EDIT_MODE, true)
        extras.putSerializable(EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(extras, CreateCommerceActivity::class.java, false)
    }

    private fun goCommerceDeals(commerceId: String) {
        val extras = Bundle()
        extras.putString(EXTRA_COMMERCE_ID, commerceId)
        openActivityWithBundle(extras, DealCommerceActivity::class.java, false)
    }

    private fun deleteDialog(commerceId: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.message_delete_commerce_dialog))
        alertDialog.setPositiveButton(R.string.label_delete) { _, _ ->
            viewModel.deleteCommerce(commerceId)
            editCommerceDialog?.dismiss()
        }
        alertDialog.setNegativeButton(R.string.label_cancel) { _, _ ->
            editCommerceDialog?.dismiss()
        }
        alertDialog.show()
        alertDialog.create()
    }
}