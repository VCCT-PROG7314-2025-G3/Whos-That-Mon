package com.poe.whosthatmon.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poe.whosthatmon.data.model.UnlockedPokemon
import com.poe.whosthatmon.data.model.UserEntity

@Database(entities = [UserEntity::class, UnlockedPokemon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}