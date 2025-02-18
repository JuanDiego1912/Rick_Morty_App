package com.dam2.rick_morty_app.Views

import android.os.Bundle
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
    }

    private fun fetchCharacterInformation() {
        Picasso
            .get()
            .load(intent.getStringExtra("IMAGEURL"))
            .into(b.imgvCharacter)
    }

}