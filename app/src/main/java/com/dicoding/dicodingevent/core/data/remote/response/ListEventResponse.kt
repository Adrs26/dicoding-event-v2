package com.dicoding.dicodingevent.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListEventResponse(
    @field:SerializedName("listEvents")
    val events: List<DetailEventResponse>
)
