package com.dam2.rick_morty_app.Views

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.rick_morty_app.Model.APIService
import com.dam2.rick_morty_app.Model.Episodes.Episode
import com.dam2.rick_morty_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b : ActivityMainBinding
    private val episodes = mutableListOf<String>()
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

        getEpisodesList()
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            episodes
        )

        b.spEpisodes.adapter = adapter
    }

    private fun getEpisodesList() {
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