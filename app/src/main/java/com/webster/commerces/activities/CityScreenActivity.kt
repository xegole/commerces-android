package com.webster.commerces.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.entity.City
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.services.RetrofitServices
import com.webster.commerces.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_city_screen.*

class CityScreenActivity : BaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val commercesServices by lazy {
        RetrofitServices.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_screen)

        showLoading()
        commercesServices.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { setSpinnerWithCities(it.cities) },
                { error ->
                    dismissLoading()
                    Snackbar.make(viewGroup, error.localizedMessage, Snackbar.LENGTH_LONG).show()
                    Log.d(Constants.TAG_SERVICES, error.localizedMessage)
                },
                { dismissLoading() }
            )
    }

    private fun setSpinnerWithCities(listCities: List<City>) {
        spinnerCities!!.onItemSelectedListener = this
        val adapterCities = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCities)
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCities!!.adapter = adapterCities
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val city = parent!!.getItemAtPosition(position) as City
        Log.d(Constants.TAG_SERVICES, "selected an item => ${city.name}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Do nothing
    }

    override fun onClick(v: View?) {
        goToActivity(HomeScreenActivity::class.java)
    }
}
