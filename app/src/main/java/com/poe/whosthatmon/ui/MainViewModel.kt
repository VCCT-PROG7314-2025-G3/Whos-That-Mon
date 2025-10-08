package com.poe.whosthatmon.ui

import androidx.lifecycle.*
import com.poe.whosthatmon.data.api.RetrofitInstance
import com.poe.whosthatmon.data.model.Pokemon
import com.poe.whosthatmon.data.model.PokemonApiResponse
import com.poe.whosthatmon.data.repository.PokemonRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> get() = _pokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchRandomPokemon() {
        viewModelScope.launch {
            try {
                val localPokemon = repository.getLocalPokemon()
//                val randomPokemonId = Random.nextInt(1, 151)
//                val response = repository.getPokemon(randomPokemonId)
//
//                if (response.id == null || response.name == null) {
//                    _error.postValue("Failed to parse Pokémon data.")
//                    return@launch
//                }
//
//                val imageUrl = response.sprites?.other?.officialArtwork?.frontDefault ?: response.sprites?.frontDefault
//
//                val simplePokemon = Pokemon(
//                    id = response.id,
//                    name = response.name,
//                    imageUrl = imageUrl
//                )
                _pokemon.postValue(localPokemon)
            } catch (e: Exception) {
                _error.postValue("Failed to fetch Pokémon: ${e.message}")
            }
        }
    }
}