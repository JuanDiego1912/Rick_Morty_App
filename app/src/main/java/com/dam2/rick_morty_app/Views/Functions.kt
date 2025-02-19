package com.dam2.rick_morty_app.Views

import com.dam2.rick_morty_app.Model.Episodes.Episode

fun getIdEpisode(episodio : String): Int {
    return episodio.split(".")[0].trim().toIntOrNull() ?: -1
}

fun getCharactersListFromSelectedEpisode(id : Int, episodeList : List<Episode>) : List<String> {
    return episodeList.find { it.id == id  }?.personajes ?: emptyList()
}