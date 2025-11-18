package com.poe.whosthatmon.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.poe.whosthatmon.data.db.PokemonDao
import com.poe.whosthatmon.data.network.PokeApiClient
import com.poe.whosthatmon.data.db.Pokemon as LocalPokemon
import com.poe.whosthatmon.data.model.UnlockedPokemon
import me.sargunvohra.lib.pokekotlin.model.Pokemon as ApiPokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokemonDao: PokemonDao) {

    private val pokeApiService = PokeApiClient.client

    // The UI will observe this LiveData, which comes directly from the local database.
    val allPokemonFromDb: LiveData<List<LocalPokemon>> = pokemonDao.getAllPokemon()

    /**
     * Checks if the local database is empty. If so, fetches the first 151 Pokémon
     * from the API and populates the database for offline use.
     */
    suspend fun populateDatabaseIfNecessary() {
        if (pokemonDao.getMasterListCount() == 0) {
            Log.d("Repository", "Database is empty. Fetching from network...")
            try {
                // We'll fetch all 151 at once.
                val pokemonToInsert = withContext(Dispatchers.IO) {
                    (1..151).map { id ->
                        val pokemon: ApiPokemon = pokeApiService.getPokemon(id)
                        LocalPokemon(
                            id = pokemon.id,
                            name = pokemon.name,
                            spriteUrl = pokemon.sprites.frontDefault!!
                        )
                    }
                }
                pokemonDao.insertAll(pokemonToInsert)
                Log.d("Repository", "Successfully populated database with ${pokemonToInsert.size} Pokémon.")
            } catch (e: Exception) {
                Log.e("Repository", "Failed to populate database", e)
            }
        } else {
            Log.d("Repository", "Database already has data. Skipping network fetch.")
        }
    }

    /**
     * Marks a Pokémon as "unlocked" for the current user in the local database.
     * This is an offline operation.
     */
    suspend fun unlockPokemonForUser(uid: String, pokemonId: Int) {
        val unlockedPokemon = UnlockedPokemon(uid = uid, pokemonId = pokemonId)
        pokemonDao.unlockedPokemon(unlockedPokemon)
        Log.d("Repository", "User $uid unlocked Pokémon #$pokemonId")
    }

    /**
     * Retrieves a list of UnlockedPokemon objects for a specific user from the local database.
     */
    suspend fun getUnlockedPokemon(uid: String): List<UnlockedPokemon> {
        return withContext(Dispatchers.IO) {
            pokemonDao.getUnlockedPokemon(uid)
        }
    }

    /**
     * Fetches detailed information for the Pokédex screen.
     * This is an ONLINE-only operation.
     */
    suspend fun getDetailedPokemon(id: Int): ApiPokemon? {
        return try {
            withContext(Dispatchers.IO) {
                pokeApiService.getPokemon(id)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Failed to fetch detailed data for #$id", e)
            null
        }
    }

    /**
     * Fetches species-specific information for a Pokémon, which contains flavor texts.
     * This is an ONLINE-only operation.
     */
    suspend fun getPokemonSpeciesById(id: Int): me.sargunvohra.lib.pokekotlin.model.PokemonSpecies? {
        return try {
            withContext(Dispatchers.IO) {
                pokeApiService.getPokemonSpecies(id)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Failed to fetch species data for #$id", e)
            null
        }
    }
}

