package com.poe.whosthatmon.data.model

import com.squareup.moshi.Json

data class PokemonResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites,
)

data class TypeSlot(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)

data class Sprites(
    @param:Json(name = "front_default") val frontDefault: String?
)






