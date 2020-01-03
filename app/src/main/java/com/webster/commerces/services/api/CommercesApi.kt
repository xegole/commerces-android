package com.webster.commerces.services.api

import com.webster.commerces.responses.GetBannersResponse
import com.webster.commerces.responses.GetCategoriesResponse
import com.webster.commerces.responses.GetCitiesResponse
import com.webster.commerces.responses.GetCommercesResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CommercesApi {

    @GET("cities/allCities")
    fun getCities(): Observable<GetCitiesResponse>

    @GET("commerces/allCommerces")
    fun getCommerces(): Observable<GetCommercesResponse>

    @GET("commerces/byCategory")
    fun getCommercesByCategory(@Query("categoryId") categoryId: String): Observable<GetCommercesResponse>

    @DELETE("commerces/byCategory")
    fun deleteCommerces(@Path("categoryId") categoryId: String)

    @GET("banners")
    fun getBanners(): Observable<GetBannersResponse>

    @GET("categories")
    fun getCategories(): Observable<GetCategoriesResponse>

}