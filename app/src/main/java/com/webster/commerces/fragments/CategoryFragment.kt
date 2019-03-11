package com.webster.commerces.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webster.commerces.R
import com.webster.commerces.activities.CategoryDetailActivity
import com.webster.commerces.activities.CategoryDetailActivity.Companion.EXTRA_CATEGORY_DATA
import com.webster.commerces.adapter.CategoryAdapter
import com.webster.commerces.entity.Category
import com.webster.commerces.extensions.openActivityWithBundle
import com.webster.commerces.fragments.base.BaseFragment
import com.webster.commerces.services.RetrofitServices
import com.webster.commerces.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_category.*
import java.util.*

class CategoryFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment().apply {}
    }

    private val commercesServices by lazy {
        RetrofitServices.create()
    }

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter(ArrayList()) { category -> categoryItemClicked(category) }
        recyclerCategories.adapter = categoryAdapter

        showLoading()
        commercesServices.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { categoryAdapter.addItemList(it.categories) },
                { error ->
                    dismissLoading()
                    Snackbar.make(viewGroup, error.localizedMessage, Snackbar.LENGTH_LONG).show()
                    Log.d(Constants.TAG_SERVICES, error.localizedMessage)
                },
                { dismissLoading() }
            )
    }

    private fun categoryItemClicked(category: Category) {
        val extras = Bundle()
        extras.putSerializable(EXTRA_CATEGORY_DATA, category)
        openActivityWithBundle(extras, CategoryDetailActivity::class.java)
    }
}