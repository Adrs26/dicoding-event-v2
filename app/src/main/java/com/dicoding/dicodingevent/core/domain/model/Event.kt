package com.dicoding.dicodingevent.core.domain.model

import android.os.Parcelable

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val imageLogo: String,
    val mediaCover: String,
    val category: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String,
    val link: String
)