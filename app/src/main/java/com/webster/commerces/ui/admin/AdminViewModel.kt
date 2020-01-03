package com.webster.commerces.ui.admin

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.webster.commerces.activities.CreateCategoryActivity
import com.webster.commerces.activities.CreateCommerceActivity
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.ui.categories.view.ListCategoriesActivity
import com.webster.commerces.ui.commerces.view.ListCommercesActivity

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    fun onClickCreateCommerce() = View.OnClickListener {
        it.goActivity(CreateCommerceActivity::class.java)
    }

    fun onClickCreateCategory() = View.OnClickListener {
        it.goActivity(CreateCategoryActivity::class.java)
    }

    fun onClickEditCommerces() = View.OnClickListener {
        it.goActivity(ListCommercesActivity::class.java)
    }

    fun onClickEditCategories() = View.OnClickListener {
        it.goActivity(ListCategoriesActivity::class.java)
    }
}