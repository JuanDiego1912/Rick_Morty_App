package com.dam2.rick_morty_app.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam2.rick_morty_app.Adapters.CharactersOnEpisodeAdapter
import com.dam2.rick_morty_app.Model.API.APIService
import com.dam2.rick_morty_app.Model.Characters.CharacterResponse
import com.dam2.rick_morty_app.Model.Episodes.Episode
import com.dam2.rick_morty_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b : ActivityMainBinding
    private val episodesInSpinner = mutableListOf<String>()
    private lateinit var characterAdapter : CharactersOnEpisodeAdapter
    private var characters : List<CharacterResponse> = listOf()
    private var episodesResponse = mutableListOf<Episode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        characterAdapter = CharactersOnEpisodeAdapter(listOf())
        b.rvCharacters.layoutManager = LinearLayoutManager(this)
        b.rvCharacters.adapter = characterAdapter

        fetchEpisodesList()
    }

    private fun initRecyclerView() {
        b.rvCharacters.layoutManager = LinearLayoutManager(this)
        b.rvCharacters.adapter = CharactersOnEpisodeAdapter(characters) {
            onItemClickListener(it)
        }
    }

    private fun onItemClickListener(character : CharacterResponse) {
        val intent = Intent(this, CharacterInfo::class.java)
        intent.putExtra("IMAGEURL", character.imagen)
        intent.putExtra("NAME", character.nombre)
        intent.putExtra("STATUS", character.estado)
        intent.putExtra("SPECIES", character.especie)
        intent.putExtra("TYPE", character.tipo)
        intent.putExtra("GENDER", character.genero)
        startActivity(intent)
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            episodesInSpinner
        )

        b.spEpisodes.adapter = adapter
    }

    suspend fun fetchCharacters(characterUrls: List<String>) {

        val api = getRetrofitCharacter().create(APIService::class.java)

        val characterIds = characterUrls.map { url ->
            url.substringAfterLast("/")
        }

        coroutineScope {

            characters = characterIds.map { id ->
                async { api.getCharacter(id.toInt()) }
            }.awaitAll()

            runOnUiThread {
                initRecyclerView()
                characterAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setCharacterByEpisodeID() {
        b.spEpisodes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val idEpisode = getIdEpisode(parent?.getItemAtPosition(position).toString())

                val characterUrls = getCharactersListFromSelectedEpisode(idEpisode, episodesResponse)

                CoroutineScope(Dispatchers.IO).launch {
                    fetchCharacters(characterUrls)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchEpisodesList() {
        CoroutineScope(Dispatchers.IO).launch {
            val llamada = getRetrofitEpisodesList()
                .create(APIService::class.java)
                .getEpisodes()

            val episodesAPI = llamada.body()

            runOnUiThread {
                if (llamada.isSuccessful) {
                    episodesResponse.clear()
                    episodesResponse = (episodesAPI?.results ?: emptyList()).toMutableList()

                    val listaEpisodios : List<String> = episodesResponse.map {
                        it.id.toString() + ". " + it.episodio + " - " + it.nombreEpisodio
                    }

                    episodesInSpinner.clear()
                    episodesInSpinner.addAll(listaEpisodios)

                    initSpinner()

                    setCharacterByEpisodeID()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error. La llamada a la API no ha ido bien al obtener los episodios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}