package com.dam2.rick_morty_app.Views

import com.dam2.rick_morty_app.Model.Episodes.Episode

fun getIdEpisode(episodio : String): Int {
    return episodio.split(".")[0].trim().toInt()
}

fun getCharactersListFromSelectedEpisode(id : Int, episodeList : List<Episode>) : List<String> {

    for (episode in episodeList) {
        if (episode.id == id) return episode.personajes
    }

    return emptyList()
}