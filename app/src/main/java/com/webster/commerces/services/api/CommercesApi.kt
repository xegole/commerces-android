package com.webster.commerces.services.api

import com.webster.commerces.responses.GetBannersResponse
import com.webster.commerces.responses.GetCategoriesResponse
import com.webster.commerces.responses.GetCitiesResponse
import com.webster.commerces.responses.GetCommercesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CommercesApi {

    @GET("cities/allCities")
    fun getCities(): Observable<GetCitiesResponse>

    @GET("commerces/allCommerces")
    fun getCommerces(): Observable<GetCommercesResponse>

    @GET("commerces/byCategory")
    fun getCommercesByCategory(@Query("categoryId") categoryId: String): Observable<GetCommercesResponse>

    @GET("banners")
    fun getBanners(): Observable<GetBannersResponse>

    @GET("categories")
    fun getCategories(): Observable<GetCategoriesResponse>

}