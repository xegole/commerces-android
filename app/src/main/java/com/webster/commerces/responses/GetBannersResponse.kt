package com.webster.commerces.responses

import com.google.gson.annotations.SerializedName
import com.webster.commerces.entity.Banner
import com.webster.commerces.responses.base.BaseResponse

data class GetBannersResponse(@SerializedName("result") val banners: List<Banner>) : BaseResponse()