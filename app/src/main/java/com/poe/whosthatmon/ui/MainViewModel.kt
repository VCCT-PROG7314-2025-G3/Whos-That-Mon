package com.poe.whosthatmon.ui

import androidx.lifecycle.*
import com.poe.whosthatmon.data.api.RetrofitInstance
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val _randomPokemon = MutableLiveData<com.poe.whosthatmon.data.model.PokemonResponse>()
    val randomPokemon: LiveData<com.poe.whosthatmon.data.model.PokemonResponse> get() = _randomPokemon

    fun fetchRandomPokemon() {
        viewModelScope.launch {
            val randomPokemonId = Random.nextInt(1, 151)
            val result = RetrofitInstance.api.getPokemon(randomPokemonId)
            _randomPokemon.postValue(result)
        }
    }
}