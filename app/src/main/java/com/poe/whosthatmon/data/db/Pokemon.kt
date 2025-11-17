package com.poe.whosthatmon.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_master_list")
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val spriteUrl: String
)
