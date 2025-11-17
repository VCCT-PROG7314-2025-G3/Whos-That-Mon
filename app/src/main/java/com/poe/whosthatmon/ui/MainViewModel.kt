package com.poe.whosthatmon.ui

import androidx.lifecycle.*
import com.poe.whosthatmon.data.db.Pokemon as LocalPokemon
import com.poe.whosthatmon.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PokemonRepository) : ViewModel() {

    // This LiveData comes directly from the Room database. It's our single source of truth.
    val allLocalPokemon: LiveData<List<LocalPokemon>> = repository.allPokemonFromDb

    private val _randomPokemon = MutableLiveData<LocalPokemon>()
    val randomPokemon: LiveData<LocalPokemon> get() = _randomPokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        // When the ViewModel is created, tell the repository to check and populate the database if needed.
        viewModelScope.launch {
            repository.populateDatabaseIfNecessary()
        }
    }

    /**
     * Picks a new random Pokémon from the locally stored list. This is an offline operation.
     */
    fun fetchRandomPokemonFromLocal() {
        val pokemonList = allLocalPokemon.value
        if (!pokemonList.isNullOrEmpty()) {
            _randomPokemon.value = pokemonList.random()
        } else {
            // This might happen on first launch before the DB is populated.
            // The UI should show a loading state until allLocalPokemon has data.
            _error.value = "Preparing Pokémon list..."
        }
    }

    /**
     * When the user guesses correctly, call the repository to save the unlocked status.
     */
    fun onCorrectGuess(uid: String, pokemonId: Int) {
        viewModelScope.launch {
            repository.unlockPokemonForUser(uid, pokemonId)
        }
    }
}

