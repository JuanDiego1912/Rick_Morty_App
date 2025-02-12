package com.dam2.rick_morty_app.Model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    //Obtener los episodios
    @GET("episode")
    suspend fun getEpisodes() : Response<EpisodesResponse>

}