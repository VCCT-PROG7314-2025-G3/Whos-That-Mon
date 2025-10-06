package com.poe.whosthatmon.data.api

import com.poe.whosthatmon.data.model.PokemonResponse
import com.poe.whosthatmon.data.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id")id: Int): PokemonResponse

    @GET("pokemon-species/{id}")
    suspend fun getSpecies(@Path("id")id: Int): PokemonSpeciesResponse
}