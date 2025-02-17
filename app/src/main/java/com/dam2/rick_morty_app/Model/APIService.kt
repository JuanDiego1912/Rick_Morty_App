package com.dam2.rick_morty_app.Model

import com.dam2.rick_morty_app.Model.Episodes.EpisodesResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    //Obtener los episodios
    @GET("episode")
    suspend fun getEpisodes() : Response<EpisodesResponse>

}