package com.dicoding.dicodingevent.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class SingleEventResponse(
    @field:SerializedName("event")
    val event: DetailEventResponse
)