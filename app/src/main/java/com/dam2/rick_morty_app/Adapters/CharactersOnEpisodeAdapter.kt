package com.dam2.rick_morty_app.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.dam2.rick_morty_app.Model.Characters.CharacterResponse
import com.dam2.rick_morty_app.R
import com.dam2.rick_morty_app.databinding.ItemCharactersBinding
import com.squareup.picasso.Picasso

class CharactersOnEpisodeAdapter(private val characters : List<CharacterResponse>, val fn : (CharacterResponse) -> Unit = {}) : Adapter<CharactersOnEpisodeAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(layoutInflater.inflate(R.layout.item_characters, parent, false))
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    inner class CharacterViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val b = ItemCharactersBinding.bind(view)

        fun bind(personaje : CharacterResponse) {

            b.tvCharacterName.text = personaje.nombre

            Picasso
                .get()
                .load(personaje.imagen)
                .into(b.imgvCharacter)

            itemView.setOnClickListener {
                fn(personaje)
            }
        }
    }
}