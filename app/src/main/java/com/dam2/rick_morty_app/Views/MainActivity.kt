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
import com.dam2.rick_morty_app.R
import com.dam2.rick_morty_app.SpinnerInformationSetter.SpinnerInformation
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

        characterAdapter = CharactersOnEpisodeAdapter(emptyList())
        fetchEpisodesList()
    }

    private fun initRecyclerView() {
        characterAdapter = CharactersOnEpisodeAdapter(characters) {
            onItemClickListener(it)
        }
        b.rvCharacters.layoutManager = LinearLayoutManager(this)
        b.rvCharacters.adapter = characterAdapter
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
            R.layout.spinner_selected_item,
            episodesInSpinner
        )
        adapter.setDropDownViewResource(R.layout.spinner_list_items)

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

            val api = getRetrofitEpisodesList().create(APIService::class.java)
            val allEpisodes = mutableListOf<Episode>()
            var nextPage : String? = "1"

            while (nextPage != null) {
                val llamada = api.getNextPage(nextPage.toInt())

                if (llamada.isSuccessful) {
                    val episodeResponse = llamada.body()

                    episodeResponse?.let {
                        allEpisodes.addAll(it.results)
                        nextPage = it.info.paginaSiguiente?.substringAfter("page=") ?: null
                    }

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al obtener los episodios.",
                        Toast.LENGTH_SHORT).show()
                    break
                }
            }

            runOnUiThread {
                if (allEpisodes.isNotEmpty()) {

                    episodesResponse.clear()
                    episodesResponse.addAll(allEpisodes)

                    episodesInSpinner.clear()
                    episodesInSpinner.addAll(episodesResponse.map {
                        it.id.toString() + ". " + it.episodio + " - " + it.nombreEpisodio
                    })

                    val actualizarSpiner = SpinnerInformation(
                        b.cgSeasons,
                        b.spEpisodes,
                        episodesResponse,
                        this@MainActivity
                    )

                    actualizarSpiner.setListenerOnChips()
                    initSpinner()
                    setCharacterByEpisodeID()

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al obtener los episodios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}