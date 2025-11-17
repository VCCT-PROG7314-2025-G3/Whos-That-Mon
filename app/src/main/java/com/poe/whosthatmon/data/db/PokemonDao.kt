package com.poe.whosthatmon.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poe.whosthatmon.data.model.UnlockedPokemon
import com.poe.whosthatmon.data.model.UserEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun unlockedPokemon(Pokemon: UnlockedPokemon)

    @Query("SELECT * FROM unlockedpokemon WHERE uid = :uid")
    suspend fun getUnlockedPokemon(uid: String): List<UnlockedPokemon>

    @Query("SELECT * FROM pokemon_master_list")
    fun getAllPokemon(): LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonList: List<Pokemon>)

    @Query("SELECT COUNT(*) FROM pokemon_master_list")
    suspend fun getMasterListCount(): Int
}