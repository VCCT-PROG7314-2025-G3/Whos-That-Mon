package com.poe.whosthatmon.ui.pokedex

import androidx.lifecycle.*
import com.poe.whosthatmon.data.repository.PokemonRepository
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.model.Pokemon as ApiPokemon

class PokedexViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _unlockedPokemonDetails = MutableLiveData<List<ApiPokemon>>()
    val unlockedPokemonDetails: LiveData<List<ApiPokemon>> get() = _unlockedPokemonDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchUnlockedPokemonData(uid: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val unlockedIds = repository.getUnlockedPokemon(uid).map { it.pokemonId }
            val detailedList = mutableListOf<ApiPokemon>()

            // Fetch full details for each unlocked ID
            for (id in unlockedIds) {
                repository.getDetailedPokemon(id)?.let { detailedPokemon ->
                    detailedList.add(detailedPokemon)
                }
            }

            _unlockedPokemonDetails.postValue(detailedList.sortedBy { it.id })
            _isLoading.postValue(false)
        }
    }
}

// Create the Factory for this ViewModel
class PokedexViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokedexViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokedexViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
