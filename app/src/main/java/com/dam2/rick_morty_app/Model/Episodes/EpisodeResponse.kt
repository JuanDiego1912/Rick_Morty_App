package com.dam2.rick_morty_app.Model.Episodes

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("info") val info : Info,
    @SerializedName("results") val results : List<Episode>
)
