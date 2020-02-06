package com.webster.commerces.extensions

import androidx.lifecycle.AndroidViewModel
import com.webster.commerces.AppCore

fun AndroidViewModel.getString(resourceId: Int) = getApplication<AppCore>().getString(resourceId)