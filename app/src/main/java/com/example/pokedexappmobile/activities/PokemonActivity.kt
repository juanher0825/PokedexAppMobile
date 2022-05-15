package com.example.pokedexappmobile.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedexappmobile.databinding.ActivityPokemonBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class PokemonActivity : AppCompatActivity() {

    private var _binding: ActivityPokemonBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPokemonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        intent.extras?.getString("imgUrl")?.let { this.bitmapGenerator(it) }

        binding.PokeNameTV.text = intent.extras?.getString("name")
        binding.pokeTypeTV.text = intent.extras?.getString("type")

        binding.defStat.text = intent.extras?.getString("def")
        binding.atkStat.text = intent.extras?.getString("atk")
        binding.speedStat.text = intent.extras?.getString("speed")
        binding.healthStat.text = intent.extras?.getString("health")

        binding.freeBttn.setOnClickListener {

            val idPoke = intent.extras?.getString("id")

            if (idPoke != null) {
                Firebase.firestore.collection("pokemons").document(idPoke)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(
                            ">>> ",
                            "DocumentSnapshot successfully deleted!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(">>> ", "Error deleting document", e) }

                val intent = Intent(this, PokedexActivity::class.java).apply {
                    putExtra("username", intent.extras?.getString("trainer"))
                }
                startActivity(intent)
                finish()
            }
        }

    }




    private fun bitmapGenerator(url: String) {

        val urlImage: URL = URL(url)

        val result: Deferred<Bitmap?> = GlobalScope.async {
            urlImage.toBitmap()
        }

        GlobalScope.launch(Dispatchers.Main) {

            binding.pokemonImage.setImageBitmap(result.await())
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