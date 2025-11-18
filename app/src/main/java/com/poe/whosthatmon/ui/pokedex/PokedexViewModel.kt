package com.poe.whosthatmon.ui.pokedex

import androidx.lifecycle.*
import com.poe.whosthatmon.data.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PokedexViewModel(private val repository: PokemonRepository) : ViewModel() {

    // The LiveData now holds our clean PokedexEntry objects
    private val _unlockedPokemonDetails = MutableLiveData<List<PokedexEntry>>()
    val unlockedPokemonDetails: LiveData<List<PokedexEntry>> get() = _unlockedPokemonDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchUnlockedPokemonData(uid: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            // Get the list of unlocked IDs from the local database
            val unlockedIds = repository.getUnlockedPokemon(uid).map { it.pokemonId }

            // Asynchronously fetch all details for each unlocked Pokémon
            val detailedEntries = unlockedIds.map { id ->
                async {
                    val pokemon = repository.getDetailedPokemon(id)
                    val species = repository.getPokemonSpeciesById(id)

                    if (pokemon != null && species != null) {
                        // Find the first English flavor text entry
                        val flavorText = species.flavorTextEntries
                            .firstOrNull { it.language.name == "en" }
                            ?.flavorText
                            ?.replace("\n", " ") // Clean up newlines
                            ?: "No description available."

                        // Get the primary type (the first one in the list)
                        val primaryType = pokemon.types.firstOrNull()?.type?.name ?: "normal"

                        // Format types for display
                        val typesForDisplay = pokemon.types.joinToString(", ") { it.type.name.replaceFirstChar { char -> char.uppercase() } }

                        // Format stats
                        val stats = pokemon.stats
                            .filter { it.stat.name in listOf("hp", "attack", "defense") }
                            .joinToString(" / ") { "${it.stat.name.uppercase()}: ${it.baseStat}" }

                        // Height is in decimetres, convert to metres (divide by 10)
                        val heightInMetres = pokemon.height / 10.0
                        val heightString = "${heightInMetres}m"

                        // Weight is in hectograms, convert to kilograms (divide by 10)
                        val weightInKg = pokemon.weight / 10.0
                        val weightString = "${weightInKg}kg"
                        // --- END OF NEW LOGIC ---

                        // Create our combined PokedexEntry object with all the data
                        PokedexEntry(
                            id = pokemon.id,
                            name = pokemon.name.replaceFirstChar { it.uppercase() },
                            spriteUrl = pokemon.sprites.frontDefault!!,
                            primaryType = pokemon.types.firstOrNull()?.type?.name ?: "normal",
                            types = pokemon.types.joinToString(", ") { it.type.name.replaceFirstChar { char -> char.uppercase() } },
                            stats = pokemon.stats
                                .filter { it.stat.name in listOf("hp", "attack", "defense") }
                                .joinToString(" / ") { "${it.stat.name.uppercase()}: ${it.baseStat}" },
                            flavorText = species.flavorTextEntries.firstOrNull { it.language.name == "en" }?.flavorText?.replace("\n", " ") ?: "No description available.",
                            height = heightString, // Add height
                            weight = weightString  // Add weight
                        )
                    } else {
                        null // Return null if any API call fails
                    }
                }
            }.awaitAll().filterNotNull() // Wait for all fetches and filter out any failures

            _unlockedPokemonDetails.postValue(detailedEntries.sortedBy { it.id })
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
