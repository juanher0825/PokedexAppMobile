package com.example.pokedexappmobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedexappmobile.R
import com.example.pokedexappmobile.databinding.ActivityPokedexBinding
import com.example.pokedexappmobile.model.Pokemon
import com.example.pokedexappmobile.util.Constants
import com.example.pokedexappmobile.util.HTTPSWebUtil
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class PokedexActivity : AppCompatActivity() {

    private var _binding: ActivityPokedexBinding? = null
    private val binding get() = _binding!!

    private lateinit var  layoutManager: LinearLayoutManager
    //-private lateinit var adapter: PokemonAdapter

    private var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPokedexBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Receive username
        username = intent.extras?.getString("username")

        layoutManager = LinearLayoutManager(this)
        binding.pokemonRecycler.layoutManager = layoutManager

        //-adapter = PokemonAdapter()
        //-adapter.activityContext = this
        //-binding.pokemonRecycler.adapter = adapter


        binding.catchBttn.setOnClickListener{
            var pokemonName = binding.catchPokemonInput.text.toString()
            pokemonName = pokemonName.lowercase()



            getPokemon(pokemonName)


        }



        binding.searchBttn.setOnClickListener{

            var pokename = binding.pokemonSearcInput.text.toString()


            var pokemons: ArrayList<Pokemon> = ArrayList()
            if(pokename != ""){
                val query = Firebase.firestore.collection("pokemons").whereEqualTo("trainer", username).whereEqualTo("name", pokename)

                query.get().addOnCompleteListener { task ->
                    if(task.result?.size() != 0){
                        for(document in task.result!!){

                            val temp: Pokemon = document.toObject(Pokemon::class.java)
                            pokemons.add(temp)

                            Log.i(">>> ", temp.name)
                        }
                    }
                }

                //-adapter.setPokemons(pokemons)

            }else{
                this.loadPokemons()
            }

        }
    }


    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    private fun getPokemon(pokemonName : String){

        lifecycleScope.launch(Dispatchers.IO){


            try{

                val response = HTTPSWebUtil().GETRequest("${Constants.POKEBASE_URL}/pokemon/${pokemonName}")

                if(response != null){

                    val jsonPokemon = JSONObject(response)

                    //Pokemon name
                    val name = jsonPokemon.getString("name")

                    //Url to pokemon photos
                    val sprites = jsonPokemon.getJSONObject("sprites")
                    val spritePhoto = sprites.getString("front_default")

                    //Array with every pokemon stat
                    val stats = jsonPokemon.getJSONArray("stats")

                    //Pokemon health points
                    val jsonHP = stats.getJSONObject(0)
                    val hp = jsonHP.getString("base_stat")

                    //Pokemon attack points
                    val jsonAttack = stats.getJSONObject(1)
                    val attack = jsonAttack.getString("base_stat")

                    //Pokemon defense points
                    val jsonDefense = stats.getJSONObject(2)
                    val defense = jsonDefense.getString("base_stat")

                    //Pokemon speed points
                    val jsonSpeed = stats.getJSONObject(5)
                    val speed = jsonSpeed.getString("base_stat")

                    //Pokemon types
                    val jsonTypes = jsonPokemon.getJSONArray("types")
                    var type = ""

                    var i :Int = 0
                    while(i<jsonTypes.length()){

                        val jsonType = jsonTypes.getJSONObject(i)
                        val rawType = jsonType.getJSONObject("type")
                        type = type + rawType.getString("name") + ", "
                        i++
                    }

                    //New pokemon

                    var usernameIt = username!!

                    var newPokemon = Pokemon(UUID.randomUUID().toString(), spritePhoto, usernameIt, name, type, "${System.currentTimeMillis()}", defense, attack, speed, hp)

                    ///////////////////////////////////////////////////////////

                    //val newPokemon = Gson().fromJson(response, Pokemon::class.java)
                    val pokemonJson = Gson().toJson(newPokemon)

                    //Put the new pokemon in the database
                    Firebase.firestore.collection("pokemons").document(newPokemon.id).set(newPokemon)
                    //HTTPSWebUtil().PUTRequest("${Constants.DATA_BASE_URL}/pokemons/${newPokemon.name}.json", pokemonJson)

                    loadPokemons()
                }

            }catch (e: Exception){

            }

        }
    }

    override fun onStart() {
        this.loadPokemons()
        super.onStart()
    }


    private fun loadPokemons(){

        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                //-adapter.setPokemons(ArrayList<Pokemon>())
            }
        }

        val query = Firebase.firestore.collection("pokemons").whereEqualTo("trainer", username).orderBy("date", Query.Direction.ASCENDING)

        query.get().addOnCompleteListener { task ->

            if(task.result?.size() != 0){
                for(document in task.result!!){
                    val poke = document.toObject(Pokemon::class.java)

                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            //-adapter.addPokemon(poke)
                        }
                    }

                }
            }
        }
    }
}