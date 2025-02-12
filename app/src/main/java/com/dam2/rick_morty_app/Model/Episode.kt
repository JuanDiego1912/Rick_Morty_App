package com.dam2.rick_morty_app.Model

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val nombreEpisodio : String,
    @SerializedName("episode") val episodio : String,
    @SerializedName("characters") val personajes : List<String>,
    @SerializedName("url") val urlEpisodio : String,
)
