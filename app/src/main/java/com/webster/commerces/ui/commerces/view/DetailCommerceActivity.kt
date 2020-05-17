package com.webster.commerces.ui.commerces.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.snackbar.Snackbar
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.adapter.ImagePagerAdapter
import com.webster.commerces.databinding.ActivityDetailCommerceBinding
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.loadUrl
import com.webster.commerces.ui.commerces.viewmodel.DetailCommerceViewModel
import com.webster.commerces.ui.rate.RateDialog
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_detail_commerce.*


class DetailCommerceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COMMERCE_DATA = "extra_commerce_data"
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailCommerceViewModel::class.java)
    }

    private val prefs by lazy {
        AppCore.prefs
    }

    private lateinit var binding: ActivityDetailCommerceBinding

    private lateinit var commerce: Commerce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_commerce)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(toolbar)
        supportPostponeEnterTransition()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        intent.extras?.let {
            commerce = it.getSerializable(EXTRA_COMMERCE_DATA) as Commerce
            supportActionBar?.title = commerce.name
            imageCommerce.loadUrl(commerce.urlImage()) {
                onSuccess {
                    supportStartPostponedEnterTransition()
                }
                onError {
                    supportStartPostponedEnterTransition()
                    toolbarLayout.visibility = View.VISIBLE
                }
            }

            labelRating.text = rating().toString()
            ratingBar.rating = rating()

            viewPagerImages.adapter = ImagePagerAdapter(applicationContext, commerce.images)
            viewPagerImages.setEnableSwipe(true)

            binding.fabMenuSocial.setMenuListener(object : SimpleMenuListenerAdapter() {
                override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
                    navigationMenu?.let {
                        validateMenuItems(navigationMenu, commerce)
                    }
                    return true
                }

                override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                    menuItem?.let {
                        viewModel.onClickItemMenu(menuItem, commerce)
                    }
                    return false
                }
            })
            viewModel.initCommerceData(commerce)
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.liveDataSocialAction.observe(this, Observer {
            if (it.resolveActivity(packageManager) != null) {
                startActivity(it)
            } else {
                showError(R.string.no_result_found)
            }
        })

        viewModel.liveDataErrorMessage.observe(this, Observer {
            showError(it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        prefs.user?.let {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_detail_commerce, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rateCommerce -> showDialog()
            android.R.id.home -> supportFinishAfterTransition()
        }
        return true
    }

    private fun showError(resourceId: Int) {
        Snackbar.make(binding.parent, getString(resourceId), Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("RestrictedApi")
    private fun validateMenuItems(navigationMenu: NavigationMenu, commerce: Commerce) {
        navigationMenu.nonActionItems.forEach { actionItem ->
            val visible = when (actionItem.itemId) {
                R.id.action_facebook -> commerce.facebook.isNotBlank()
                R.id.action_whatsapp -> commerce.whatsapp.isNotBlank()
                R.id.action_instagram -> commerce.instagram.isNotBlank()
                R.id.action_web_page -> commerce.webPage.isNotBlank()
                R.id.action_email -> commerce.email.isNotBlank()
                else -> false
            }
            actionItem.isVisible = visible
        }
    }

    private fun showDialog() {
        val rateDialog = RateDialog(this, commerce.commerceId)
        rateDialog.show()
    }

    private fun rating(): Float {
        val list = commerce.comments.map { it.value }
        return if (list.isEmpty()) {
            3.5f
        } else {
            var average = 0f
            for (comment in list) {
                average += comment.rate
            }
            average / list.size
        }
    }
}