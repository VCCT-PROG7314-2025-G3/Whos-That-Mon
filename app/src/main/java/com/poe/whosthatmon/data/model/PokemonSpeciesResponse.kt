package com.poe.whosthatmon.data.model

import com.squareup.moshi.Json

data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    @Json(name = "flavor_text") val flavorText: String,
    val language: LanguageInfo,
    val version: VersionInfo
)

data class LanguageInfo(
    val name: String,
    val url: String
)

data class VersionInfo(
    val name: String,
    val url: String
)
