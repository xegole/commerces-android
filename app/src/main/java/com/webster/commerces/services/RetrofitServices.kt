package com.webster.commerces.services

import com.webster.commerces.services.api.CommercesApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServices {

    const val BASE_URL = "http://192.168.100.6:3000/"

    fun create(): CommercesApi {

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(CommercesApi::class.java)
    }
}