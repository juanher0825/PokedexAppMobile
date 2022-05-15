package com.example.pokedexappmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pokedexappmobile.R
import com.example.pokedexappmobile.databinding.ActivityHomeBinding
import com.example.pokedexappmobile.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.loginBTN.setOnClickListener(::login)

    }


    private fun login(view : View){

        val username = binding.nameBox.text.toString()
        var user = User(UUID.randomUUID().toString(), username, System.currentTimeMillis())

        val query = Firebase.firestore.collection("users").whereEqualTo("name", username)

        query.get().addOnCompleteListener { task ->

            if(task.result?.size() == 0){
                Firebase.firestore.collection("users").document(user.id).set(user)
            }
            else{
                for(document in task.result!!){
                    user = document.toObject(User::class.java)
                    break
                }
            }
        }

        val intent = Intent(this, PokedexActivity::class.java).apply {
            putExtra("username", user.name)
        }
        startActivity(intent)
        finish()
    }
}