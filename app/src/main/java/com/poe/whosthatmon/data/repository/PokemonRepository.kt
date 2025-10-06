package com.poe.whosthatmon.data.repository

import com.poe.whosthatmon.data.api.RetrofitInstance
import com.poe.whosthatmon.data.db.PokemonDao
import com.poe.whosthatmon.data.model.PokemonResponse
import com.poe.whosthatmon.data.model.PokemonSpeciesResponse
import com.poe.whosthatmon.data.model.UnlockedPokemon
import com.poe.whosthatmon.data.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val dao: PokemonDao) {

    // Fetch Pokemon info (types, sprites)
    suspend fun getPokemon(id: Int): PokemonResponse = withContext(Dispatchers.IO){
        RetrofitInstance.api.getPokemon(id)
    }

    // Fetch Pokemon species info (pokedex entries)
    suspend fun getPokemonSpecies(id: Int): PokemonSpeciesResponse = withContext(Dispatchers.IO) {
        RetrofitInstance.api.getSpecies(id)
    }

    // Combine both (Pokemon + Species)
    suspend fun getFullPokemonData(id: Int): Pair<PokemonResponse, String?> = withContext(Dispatchers.IO) {
        val pokemon = RetrofitInstance.api.getPokemon(id)
        val species = RetrofitInstance.api.getSpecies(id)
        val entry = species.flavorTextEntries.firstOrNull { it.language.name == "en" }?.flavorText
        pokemon to entry
    }

    // Save user locally
    suspend fun saveUser(user: UserEntity) = withContext(Dispatchers.IO) {
        dao.insertUser(user)
    }

    // Mark a Pokemon as unlocked for this user
    suspend fun unlockPokemon(uid: String, pokemonId: Int) = withContext(Dispatchers.IO) {
        val unlocked = UnlockedPokemon(uid = uid, pokemonId = pokemonId)
        dao.unlockedPokemon(unlocked)
    }

    // Get all unlocked pokemon for a specific user
    suspend fun getUnlockedPokemon(uid: String): List<UnlockedPokemon> = withContext(Dispatchers.IO) {
        dao.getUnlockedPokemon(uid)
    }
}