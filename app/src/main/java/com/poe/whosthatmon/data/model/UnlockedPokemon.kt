package com.poe.whosthatmon.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "pokemonId"])
data class UnlockedPokemon(
    val uid: String,
    val pokemonId: Int
)
