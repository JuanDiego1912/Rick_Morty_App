package com.dam2.rick_morty_app.Model.API

import com.dam2.rick_morty_app.Model.Characters.CharacterResponse
import com.dam2.rick_morty_app.Model.Episodes.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    //Obtener los episodios
    @GET("episode")
    suspend fun getEpisodes() : Response<EpisodeResponse>

    //Obtener la informacion de un personaje por su ID
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") characterId: Int): CharacterResponse

    @GET("?page={idPage}")
    suspend fun getNextPage(@Path("idPage") pageNumber : Int) : Response<EpisodeResponse>
}