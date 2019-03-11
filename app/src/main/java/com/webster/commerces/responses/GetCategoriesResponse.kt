package com.webster.commerces.responses

import com.google.gson.annotations.SerializedName
import com.webster.commerces.entity.Category
import com.webster.commerces.responses.base.BaseResponse

data class GetCategoriesResponse(@SerializedName("result")val categories: List<Category>) : BaseResponse()