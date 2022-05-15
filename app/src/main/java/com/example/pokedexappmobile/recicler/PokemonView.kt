package com.example.pokedexappmobile.recicler

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexappmobile.R
import com.example.pokedexappmobile.activities.PokemonActivity
import com.example.pokedexappmobile.model.Pokemon

class PokemonView(itemView: View): RecyclerView.ViewHolder(itemView) {

    var pokemon: Pokemon? = null

    var pokemonImage: ImageView = itemView.findViewById(R.id.pokeRowImage)
    var pokemonName: TextView = itemView.findViewById(R.id.pokeRowName)

    var context: Context? = null

    init {
        pokemonImage.setOnClickListener{

            Log.i(context.toString(), pokemon.toString())

            if(context != null && pokemon != null){
                val intent = Intent(context, PokemonActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                intent.putExtra("imgUrl", pokemon!!.imgUrl)
                intent.putExtra("name", pokemon!!.name)
                intent.putExtra("trainer", pokemon!!.trainer)
                intent.putExtra("type", pokemon!!.types)

                intent.putExtra("id", pokemon!!.id)

                intent.putExtra("def", pokemon!!.defense)
                intent.putExtra("atk", pokemon!!.attack)
                intent.putExtra("speed", pokemon!!.speed)
                intent.putExtra("health", pokemon!!.health)

                context!!.startActivity(intent)
            }

            }
    }
}