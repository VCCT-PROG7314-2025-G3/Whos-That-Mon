package com.poe.whosthatmon.data.model

import com.squareup.moshi.Json
import retrofit2.http.Url

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String?
)

// This class matches the API response. All optional fields are nullable.
data class PokemonApiResponse(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "sprites") val sprites: Sprites? // The whole sprites object can be null
)

data class Sprites(
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "other") val other: OtherSprites? // The 'other' object can be null
)

data class OtherSprites(
    @Json(name = "official-artwork") val officialArtwork: OfficialArtwork? // The 'official-artwork' can be null
)

data class OfficialArtwork(
    @Json(name = "front_default") val frontDefault: String? // The artwork URL itself can be null
)