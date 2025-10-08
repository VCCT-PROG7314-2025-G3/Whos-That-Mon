package com.poe.whosthatmon.data

import com.poe.whosthatmon.data.model.Pokemon

object LocalPokemonDataSource {
    private val localPokemonList = listOf(
        Pokemon(id = 1, name = "Bulbasaur", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"),
        Pokemon(id = 4, name = "Charmander", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"),
        Pokemon(id = 7, name = "Squirtle", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"),
        Pokemon(id = 25, name = "Pikachu", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"),
        Pokemon(id = 39, name = "Jigglypuff", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/39.png"),
        Pokemon(id = 52, name = "Meowth", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/52.png"),
        Pokemon(id = 143, name = "Snorlax", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/143.png"),
        Pokemon(id = 150, name = "Mewtwo", imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/150.png")
    )

    fun getRandomPokemon(): Pokemon {
        return localPokemonList.random()
    }

    fun getAllPokemon(): List<Pokemon> {
        return localPokemonList
    }

    fun getPokemonById(id: Int): Pokemon? {
        return localPokemonList.find { it.id == id }
    }
}