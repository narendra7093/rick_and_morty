package com.example.test.apiservice

import com.example.test.models.CharacterResponse
import com.example.test.models.Result
import retrofit2.http.GET

interface Apiservice {

    @GET("character")
    suspend fun getcharacterdata():CharacterResponse
}