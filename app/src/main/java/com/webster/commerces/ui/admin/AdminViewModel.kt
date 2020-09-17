package com.webster.commerces.ui.admin

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.webster.commerces.ui.categories.view.CreateCategoryActivity
import com.webster.commerces.activities.NotificationActivity
import com.webster.commerces.extensions.goActivity
import com.webster.commerces.ui.categories.view.ListCategoriesActivity
import com.webster.commerces.ui.cityselector.view.CreateCityActivity
import com.webster.commerces.ui.commerces.view.CreateCommerceActivity
import com.webster.commerces.ui.commerces.view.ListCommercesActivity
import com.webster.commerces.ui.commerces.view.ValidateCommercesActivity
import com.webster.commerces.ui.emergency.view.CreateEmergencyActivity
import com.webster.commerces.ui.emergency.view.EmergencyListActivity

class AdminViewModel(application: Application) : AndroidViewModel(application) {


    fun onClickValidateCommerces() = View.OnClickListener {
        it.goActivity(ValidateCommercesActivity::class.java)
    }

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

    fun onClickCreateEmergency() = View.OnClickListener {
        it.goActivity(CreateEmergencyActivity::class.java)
    }

    fun onClickEditEmergency() = View.OnClickListener {
        it.goActivity(EmergencyListActivity::class.java)
    }

    fun OnClickSendNotification() = View.OnClickListener {
        it.goActivity(NotificationActivity::class.java)
    }

    fun onClickCreateCity() = View.OnClickListener {
        it.goActivity(CreateCityActivity::class.java)
    }
}