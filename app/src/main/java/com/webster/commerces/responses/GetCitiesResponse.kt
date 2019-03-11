package com.webster.commerces.responses

import com.google.gson.annotations.SerializedName
import com.webster.commerces.entity.City
import com.webster.commerces.responses.base.BaseResponse

data class GetCitiesResponse(@SerializedName("result") val cities: List<City>) : BaseResponse()