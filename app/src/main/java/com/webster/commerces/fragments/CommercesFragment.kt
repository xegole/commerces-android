package com.webster.commerces.fragments


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.activities.DetailCommerceActivity
import com.webster.commerces.activities.DetailCommerceActivity.Companion.EXTRA_COMMERCE_DATA
import com.webster.commerces.adapter.BannerPagerAdapter
import com.webster.commerces.adapter.CommercesAdapter
import com.webster.commerces.entity.Banner
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.openActivityWithBundleOptions
import com.webster.commerces.fragments.base.BaseFragment
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.fragment_commerces.*
import java.util.*
import kotlin.collections.ArrayList

class CommercesFragment : BaseFragment() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)
    private val bannerReference = database.getReference(FirebaseReferences.BANNERS)

    private lateinit var commercesAdapter: CommercesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commerces, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commercesAdapter = CommercesAdapter(ArrayList()) { commerce, v -> commerceItemClicked(commerce, v) }
        recyclerCommerces.adapter = commercesAdapter
        initObservers()
    }

    private fun initObservers() {
        showLoading()
        commercesReference.addListDataListener<Commerce> { list, success ->
            if (success) {
                commercesAdapter.addItemList(list)
            }
            dismissLoading()
        }

        bannerReference.addListDataListener<Banner> { list, success ->
            if (success) {
                setViewPagerWithBanners(list)
            }
        }

        val listImages = ArrayList<String>()
        listImages.add("https://10619-2.s.cdn12.com/rests/small/w320/h220/107_330419770.jpg")
        listImages.add("https://lh3.googleusercontent.com/p/AF1QipPO4RazUommX-_m-wguI24wjLEKGSCiL03GzYX9=s1600-h380")

        val commerce = Commerce(
            "Comercio Test 1",
            "1",
            1234567890,
            "1",
            "commerces%2F1%2Fmaxresdefault.jpg?alt=media&token=d260bcbc-a595-4262-865c-95727cdf40cb",
            "Carrera 1 #23-45",
            "1",
            "this a test description",
            listImages
        )

        commercesReference.child("1").setValue(commerce)
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

    companion object {
        private var currentPage = Constants.INT_ZERO

        fun newInstance() = CommercesFragment()
    }
}
