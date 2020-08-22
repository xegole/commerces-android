package com.webster.commerces.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.ui.commerces.view.*
import com.webster.commerces.ui.commerces.viewmodel.AdminCommerceViewModel
import com.webster.commerces.ui.deal.view.DealCommerceActivity
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.activity_admin_commerce.*

class CommercesOwnFragment : BaseFragment(), SearchView.OnQueryTextListener {

    private val viewModel by lazy {
        ViewModelProvider(this).get(AdminCommerceViewModel::class.java)
    }

    private var editCommerceDialog: EditCommerceDialog? = null

    private val adapter by lazy {
        CommercesAdapter { commerce, _ ->
            editCommerceDialog =
                EditCommerceDialog(requireActivity()) {
                    when (it) {
                        EditState.EDIT -> goEditCommerce(commerce)
                        EditState.DELETE -> deleteDialog(commerce.commerceId)
                        EditState.DEAL -> goCommerceDeals(commerce)
                    }
                }
            editCommerceDialog?.show()
        }
    }

    private val prefs by lazy {
        Prefs(requireActivity())
    }

    override fun resourceLayout() = R.layout.activity_admin_commerce

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCommercesByUuid(prefs.user?.uid)
        setHasOptionsMenu(true)
        initViews()
        initObservers()
    }

    private fun initViews() {
        recyclerCommerces.adapter = adapter
        buttonCreateCommerce.setOnClickListener {
            if (adapter.itemCount <= 2) {
                goToActivity(CreateCommerceActivity::class.java)
            }
        }
    }

    private fun initObservers() {
        viewModel.commercesListData.observe(viewLifecycleOwner, Observer {
            adapter.addItemList(it)
        })
    }

    companion object {
        fun instance() = CommercesOwnFragment()
    }

    private fun goEditCommerce(commerce: Commerce) {
        val extras = Bundle()
        extras.putBoolean(EXTRA_EDIT_MODE, true)
        extras.putSerializable(EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(extras, CreateCommerceActivity::class.java)
        editCommerceDialog?.dismiss()
    }

    private fun goCommerceDeals(commerce: Commerce) {
        val extras = Bundle()
        extras.putSerializable(EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundle(extras, DealCommerceActivity::class.java)
        editCommerceDialog?.dismiss()
    }

    private fun deleteDialog(commerceId: String) {
        val alertDialog = AlertDialog.Builder(requireActivity())
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_search_view_menu, menu)
        val searchItem = menu.findItem(R.id.search_view)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return true
    }
}