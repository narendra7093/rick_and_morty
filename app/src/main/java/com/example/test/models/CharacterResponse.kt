package com.example.test.models


import java.io.Serializable
data class CharacterResponse(
    val info: Info,
    val results: List<Result>
) : Serializable