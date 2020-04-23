package com.webster.commerces.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.Category
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.services.RetrofitServices
import com.webster.commerces.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_commerce.*
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*

class CategoryDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_CATEGORY_DATA = "extra_category_data"
    }

    private val commercesServices by lazy {
        RetrofitServices.create()
    }

    private lateinit var commercesAdapter: CommercesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        intent.extras?.let { bundle ->
            val category = bundle.getSerializable(EXTRA_CATEGORY_DATA) as Category
            supportActionBar?.title = category.name

            commercesAdapter = CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
            recyclerCommerces.adapter = commercesAdapter

            commercesServices.getCommercesByCategory(category.categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { commercesAdapter.addItemList(it.commerces) },
                    { error ->
                        dismissLoading()
                        Snackbar.make(viewGroup, error.localizedMessage, Snackbar.LENGTH_LONG).show()
                        Log.d(Constants.TAG_SERVICES, error.localizedMessage)
                    },
                    { dismissLoading() }
                )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun commerceItemClicked(commerce: Commerce, view: View) {
        val bundle = Bundle()
        bundle.putSerializable(DetailCommerceActivity.EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundleOptions(view, bundle, DetailCommerceActivity::class.java)
    }
}
