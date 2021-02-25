package com.example.kalenderapi

import com.google.gson.annotations.SerializedName

data class ApiData(val dateInformation : List<apiDays>)

data class apiDays(val dagar : List<apiDateInfo>)

data class apiDateInfo(val datum : String, val veckodag: String,
                       @SerializedName("röd dag") val rodDag: String, val helgdag : String?)
