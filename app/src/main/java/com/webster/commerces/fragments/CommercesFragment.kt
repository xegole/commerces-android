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
import com.webster.commerces.entity.Category
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
    private val categoriesReference = database.getReference(FirebaseReferences.CATEGORIES)

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

        //commerceMock()
        // categoryMock()
    }

    private fun commerceMock() {
        val listImages = ArrayList<String>()
        listImages.add("http://plazadejuan.com/wp-content/uploads/2016/06/carniceria-en-plaza-de-juan-carne-supermercados-en-murcia-1.jpg")
        listImages.add("https://media-cdn.tripadvisor.com/media/photo-s/0c/d9/13/4a/carniceria-bom-para-conhecer.jpg")

        val commerce = Commerce(
            "Espiga Dorada",
            "1",
            3007661223,
            "1",
            "commerces%2Fepiga%20dorada%2Fbanner.jpg?alt=media&token=d1dd89d4-95c2-4763-923c-b58c8380cc6c",
            "Carrera 6 #3-45",
            "2",
            "El mejor pan de la region",
            listImages
        )
        commercesReference.push().setValue(commerce)
    }

    private fun categoryMock() {
        val category = Category(
            "1",
            "Panaderia",
            "category_images%2Fpanaderia.jpeg?alt=media&token=14e11c93-534d-47b7-85f5-095c1f8be043",
            "test"
        )
        //categoriesReference.child(category.categoryId).setValue(category)
        categoriesReference.push().setValue(category)

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
