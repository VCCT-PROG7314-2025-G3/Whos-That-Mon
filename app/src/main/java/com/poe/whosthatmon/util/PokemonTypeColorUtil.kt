package com.poe.whosthatmon.util

import android.graphics.Color

object PokemonTypeColorUtil {

    private val typeColorMap = mapOf(
        "normal" to "#AAA67F",
        "fire" to "#F57D31",
        "water" to "#6493EB",
        "electric" to "#F9CF30",
        "grass" to "#74CB48",
        "ice" to "#9AD6DF",
        "fighting" to "#C12239",
        "poison" to "#A43E9E",
        "ground" to "#DEC16B",
        "flying" to "#A891EC",
        "psychic" to "#FB5584",
        "bug" to "#A7B723",
        "rock" to "#B69E31",
        "ghost" to "#70559B",
        "dragon" to "#7037FF",
        "dark" to "#75574C",
        "steel" to "#B7B9D0",
        "fairy" to "#E69EAC"
    )

    fun getColorForType(type: String): Int {
        val colorString = typeColorMap[type.lowercase()] ?: "#AAA67F" // Default to Normal type color
        return Color.parseColor(colorString)
    }
}