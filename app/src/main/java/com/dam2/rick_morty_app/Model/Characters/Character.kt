package com.dam2.rick_morty_app.Model.Characters

import com.google.gson.annotations.SerializedName

data class Character(
    val id : Int,
    @SerializedName("name") val nombre : String,
    @SerializedName("status") val estado : String,
    @SerializedName("species") val especie : String,
    @SerializedName("type") val tipo : String,
    @SerializedName("gender") val genero : String,
    @SerializedName("image") val imagen : String
)