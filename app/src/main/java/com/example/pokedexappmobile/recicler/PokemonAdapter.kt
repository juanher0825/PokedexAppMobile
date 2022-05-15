package com.example.pokedexappmobile.recicler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexappmobile.R
import com.example.pokedexappmobile.model.Pokemon
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class PokemonAdapter: RecyclerView.Adapter<PokemonView>() {

    private var pokemons = ArrayList<Pokemon>()
    var activityContext: Context? = null

    fun addPokemon(poke: Pokemon){
        this.pokemons.add(poke)
        notifyItemInserted(this.pokemons.size-1)
    }

    fun setPokemons(pokes: ArrayList<Pokemon>){

        pokemons = pokes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonView {
        var inflater = LayoutInflater.from(parent.context)
        val pos = inflater.inflate(R.layout.pokemon_row, parent, false)
        val pokemonView = PokemonView(pos)
        return pokemonView
    }

    override fun onBindViewHolder(holder: PokemonView, position: Int) {
        val pokemon = this.pokemons[position]
        holder.pokemon = pokemon

        holder.context = activityContext

        this.bitmapGenerator(pokemon.imgUrl, holder)

        holder.pokemonName.text = pokemon.name
    }

    override fun getItemCount(): Int {
        return this.pokemons.size
    }

    private fun bitmapGenerator(url: String, holder: PokemonView) {

        val urlImage: URL = URL(url)

        val result: Deferred<Bitmap?> = GlobalScope.async {
            urlImage.toBitmap()
        }

        GlobalScope.launch(Dispatchers.Main) {

            holder.pokemonImage.setImageBitmap(result.await())
        }
    }

    fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }

}