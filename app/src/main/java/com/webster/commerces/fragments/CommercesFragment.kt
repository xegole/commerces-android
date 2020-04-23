package com.webster.commerces.fragments


import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webster.commerces.R
import com.webster.commerces.activities.DetailCommerceActivity
import com.webster.commerces.activities.DetailCommerceActivity.Companion.EXTRA_COMMERCE_DATA
import com.webster.commerces.adapter.BannerPagerAdapter
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.entity.Banner
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.fragments.base.BaseFragment
import com.webster.commerces.services.RetrofitServices
import com.webster.commerces.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*

class CommercesFragment : BaseFragment() {

    companion object {
        private var currentPage = Constants.INT_ZERO

        @JvmStatic
        fun newInstance() = CommercesFragment().apply {}
    }

    private val commercesServices by lazy {
        RetrofitServices.create()
    }

    private lateinit var commercesAdapter: CommercesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commerces, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        commercesAdapter = CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
        recyclerCommerces.adapter = commercesAdapter

        commercesServices.getCommerces()
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

        commercesServices.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { setViewPagerWithBanners(it.banners) }
    }

    private val handler = Handler()
    private lateinit var update: Runnable

    private val swipeTimer = Timer()

    private fun setViewPagerWithBanners(banners: List<Banner>) {
        viewPagerBanner.adapter = context?.let { ctx -> BannerPagerAdapter(ctx, banners) }
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

    override fun onPause() {
        super.onPause()
        swipeTimer.cancel()
    }

    private fun commerceItemClicked(commerce: Commerce, view: View) {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_COMMERCE_DATA, commerce)
        openActivityWithBundleOptions(view, bundle, DetailCommerceActivity::class.java)
    }
}
