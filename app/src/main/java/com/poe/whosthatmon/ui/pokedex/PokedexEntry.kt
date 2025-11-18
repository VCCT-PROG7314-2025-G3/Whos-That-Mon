package com.poe.whosthatmon.ui.pokedex

data class PokedexEntry(
    val id: Int,
    val name: String,
    val spriteUrl: String?,
    val primaryType: String,
    val types: String,
    val stats: String,
    val flavorText: String,
    val height: String,
    val weight: String
)
