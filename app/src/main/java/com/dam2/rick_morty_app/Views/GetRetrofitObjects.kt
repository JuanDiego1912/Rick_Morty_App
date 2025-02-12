package com.dam2.rick_morty_app.Views

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofitEpisodesList() : Retrofit {
    return Retrofit
        .Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}