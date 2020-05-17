package com.webster.commerces.fragments


import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.adapter.BannerPagerAdapter
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.entity.Banner
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import com.webster.commerces.utils.Prefs
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*
import kotlin.collections.ArrayList
import com.webster.commerces.ui.commerces.view.DetailCommerceActivity.Companion.EXTRA_COMMERCE_DATA as EXTRA_COMMERCE_DATA1

class CommercesFragment : BaseFragment() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)
    private val bannerReference = database.getReference(FirebaseReferences.BANNERS)
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

    private val adapter by lazy {
        CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
    }

    private val prefs by lazy {
        Prefs(requireContext())
    }

    override fun resourceLayout() = R.layout.fragment_commerces

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerCommerces.adapter = adapter
        initObservers()
    }

    private fun initObservers() {
        showLoading()
        if (prefs.cityId.isNotEmpty()) {
            commercesReference.orderByChild("cityId").equalTo(prefs.cityId)
                .addListDataListener<Commerce> { list, success ->
                    if (success) {
                        adapter.addItemList(list)
                    }
                    dismissLoading()
                }
        } else {
            commercesReference.orderByChild("cityId").equalTo(prefs.remember)
                .addListDataListener<Commerce> { list, b ->
                    if (b) {
                        adapter.addItemList(list)
                    }
                    dismissLoading()
                }
        }
        bannerReference.addListDataListener<Banner> { list, success ->
            if (success) {
                setViewPagerWithBanners(list)
            }
        }
    }

    private val handler = Handler()
    private lateinit var update: Runnable

    private val swipeTimer = Timer()

    private fun setViewPagerWithBanners(banners: List<Banner>? = arrayListOf()) {
        banners?.let {
            viewPagerBanner.adapter = context?.let { ctx -> BannerPagerAdapter(ctx, it) }
            indicatorBanner.setViewPager(viewPagerBanner)

            update = Runnable {
                if (currentPage == banners.size) {
                    currentPage = Constants.INT_ZERO
                }
                viewPagerBanner?.setCurrentItem(currentPage++, true)
            }
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, Constants.LONG_ZERO, Constants.DELAY_BANNER)
        }
    }

    override fun onPause() {
        super.onPause()
        swipeTimer.cancel()
    }

    private fun commerceItemClicked(commerce: Commerce, view: View) {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_COMMERCE_DATA1, commerce)
        openActivityWithBundleOptions(view, bundle, DetailCommerceActivity::class.java)
    }

    companion object {
        private var currentPage = Constants.INT_ZERO
        fun instance() = CommercesFragment()
    }
}
