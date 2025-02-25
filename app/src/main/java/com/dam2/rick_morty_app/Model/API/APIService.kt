package com.dam2.rick_morty_app.Model.API

import com.dam2.rick_morty_app.Model.Characters.CharacterResponse
import com.dam2.rick_morty_app.Model.Episodes.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    //Obtener la informacion de un personaje por su ID
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") characterId: Int): CharacterResponse

    //Obtener las siguientes páginas
    @GET("episode")
    suspend fun getNextPage(@Query("page") pageNumber: Int): Response<EpisodeResponse>
}