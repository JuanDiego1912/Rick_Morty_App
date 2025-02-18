package com.dam2.rick_morty_app.Views

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.rick_morty_app.R
import com.dam2.rick_morty_app.databinding.ActivityCharacterInfoBinding
import com.squareup.picasso.Picasso

class CharacterInfo : AppCompatActivity() {

    private lateinit var b : ActivityCharacterInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCharacterInfoBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchCharacterInformation()

        b.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun fetchCharacterInformation() {

        b.tvCharacterName.text = intent.getStringExtra("NAME")
        b.tvCharacterGender.text = intent.getStringExtra("GENDER")
        b.tvCharacterStatus.text = intent.getStringExtra("STATUS")
        b.tvCharacterSpecies.text = intent.getStringExtra("SPECIES")

        val type = intent.getStringExtra("TYPE")

        if (type!!.isBlank()) {
            b.tvCharacterType.visibility = View.GONE
        } else {
            b.tvCharacterType.text = type
        }

        Picasso
            .get()
            .load(intent.getStringExtra("IMAGEURL"))
            .noFade()
            .into(b.imgvCharacter)
    }

}