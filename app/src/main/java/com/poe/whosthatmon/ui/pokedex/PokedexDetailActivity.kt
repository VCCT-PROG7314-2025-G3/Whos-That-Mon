package com.poe.whosthatmon.ui.pokedex

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.poe.whosthatmon.data.LocalPokemonDataSource
//import com.poe.whosthatmon.data.api.RetrofitInstance
import com.poe.whosthatmon.databinding.ActivityPokedexDetailBinding
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch

class PokedexDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedexDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonId = intent.getIntExtra("POKEMON_ID", -1)
        if (pokemonId != -1) {
            loadPokemonDetails(pokemonId)
        } else {
            Toast.makeText(this, "Could not find Pokemon data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadPokemonDetails(id: Int) {
        val pokemon = LocalPokemonDataSource.getPokemonById(id)

        if (pokemon != null) {
            binding.ivPokemon.load(pokemon.imageUrl)
            binding.tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.tvPokemonId.text = "#${pokemon.id.toString().padStart(3, '0')}"

            binding.tvWeight.text = "90,49 kg"
            binding.tvHeight.text = "170.18 m"
            binding.tvPokedexEntry.text = "If Charmander becomes truly angered, the flame at the tip of its tail burns in a light blue shade."
            binding.tvTypes.text = "Fire"
        } else {
            Toast.makeText(this, "Could not find Pokemon data", Toast.LENGTH_SHORT).show()
        }
    }
}