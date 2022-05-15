package com.example.pokedexappmobile.recicler

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexappmobile.R
import com.example.pokedexappmobile.model.Pokemon

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {

    var pokemon: Pokemon? = null

    var pokemonImage: ImageView = itemView.findViewById(R.id.pokeRowImage)
    var pokemonName: TextView = itemView.findViewById(R.id.pokeRowName)

    var context: Context? = null

    init {
        pokemonImage.setOnClickListener{

            Log.i(context.toString(), pokemon.toString())

            }
    }
}