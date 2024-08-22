package com.example.test.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.models.CharacterResponse
import com.example.test.models.Result
import com.example.test.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(var repository: CharacterRepository):ViewModel() {

    var _respose = MutableLiveData<CharacterResponse>()
    val response :LiveData<CharacterResponse> = _respose

    init {
        getcharacterdata()
    }


    fun getcharacterdata()=viewModelScope.launch {
        var result = repository.Characterdata()
        _respose.value = result

        //_respose.postValue(result)



        }

}