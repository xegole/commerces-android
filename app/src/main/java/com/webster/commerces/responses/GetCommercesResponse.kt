package com.webster.commerces.responses

import com.google.gson.annotations.SerializedName
import com.webster.commerces.entity.Commerce
import com.webster.commerces.responses.base.BaseResponse

data class GetCommercesResponse(@SerializedName("result") val commerces: ArrayList<Commerce>) : BaseResponse()