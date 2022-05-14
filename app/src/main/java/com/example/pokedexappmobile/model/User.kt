package com.example.pokedexappmobile.model

import java.io.Serializable

data class User (

    var id : String = "",
    var name : String = "",
    var date : Long = 0

) : Serializable
