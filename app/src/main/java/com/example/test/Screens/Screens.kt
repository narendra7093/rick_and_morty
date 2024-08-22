package com.example.test.Screens

import com.example.test.models.CharacterResponse

sealed class Screens(val route:String) {
    object Mainscreen:Screens("main_screen")
    object DetailScreen:Screens("detail_screen")


}