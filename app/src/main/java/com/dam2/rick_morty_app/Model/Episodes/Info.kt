package com.dam2.rick_morty_app.Model.Episodes

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("count") val total : Int,
    @SerializedName("pages") val paginas : Int,
)
