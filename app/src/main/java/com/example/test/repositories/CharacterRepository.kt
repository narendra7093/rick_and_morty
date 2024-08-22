package com.example.test.repositories

import com.example.test.apiservice.Apiservice
import javax.inject.Inject

class CharacterRepository @Inject constructor(val apiservice: Apiservice) {

    suspend fun Characterdata() = apiservice.getcharacterdata()
}