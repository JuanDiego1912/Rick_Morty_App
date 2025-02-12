package com.dam2.rick_morty_app.Model

import com.google.gson.annotations.SerializedName

data class EpisodesResponse(
    @SerializedName("info") val info : Info,
    @SerializedName("results") val results : List<Episode>
)
