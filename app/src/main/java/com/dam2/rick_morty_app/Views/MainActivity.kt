package com.dam2.rick_morty_app.Views

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dam2.rick_morty_app.Adapters.CharacterAdapter
import com.dam2.rick_morty_app.Model.APIService
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
    private val episodes = mutableListOf<String>()
    private lateinit var characterAdapter: CharacterAdapter
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

        fetchEpisodesList()
    }

    private fun initRecyclerView() {
        characterAdapter = CharacterAdapter(characters)
        b.rvCharacters.layoutManager = LinearLayoutManager(this)
        b.rvCharacters.adapter = characterAdapter
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            episodes
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

            characterAdapter.notifyDataSetChanged()
            initRecyclerView()
        }
    }

    fun setCharacterByEpisodeID(characterUrls: List<String>) {
        b.spEpisodes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val episode = parent?.getItemAtPosition(position).toString()

                Toast.makeText(
                    this@MainActivity,
                    "Episodio seleccionado: ${episode}",
                    Toast.LENGTH_SHORT).show()

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

                    episodesResponse = (episodesAPI?.results ?: emptyList()).toMutableList()

                    val listaEpisodios : List<String> = episodesResponse.map { it.id.toString() + ". " + it.episodio + " - " + it.nombreEpisodio }

                    episodes.clear()
                    episodes.addAll(listaEpisodios)
                    initSpinner()

                    

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