package com.poe.whosthatmon.ui.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.poe.whosthatmon.databinding.PokedexItemLayoutBinding
// This is the rich Pokemon model from the API, not your local database model
import me.sargunvohra.lib.pokekotlin.model.Pokemon as ApiPokemon

class PokedexAdapter(
    // The adapter now directly receives a list of fully detailed Pokémon
    private var pokemonDetails: List<ApiPokemon>
) : RecyclerView.Adapter<PokedexAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PokedexItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokedexItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonDetails[position]

        with(holder.binding) {
            // --- Bind the data to your layout ---

            // Name and Number
            tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            tvPokemonNumber.text = "#${pokemon.id.toString().padStart(3, '0')}"

            // Load high-quality sprite from the API response
            val imageUrl = pokemon.sprites.frontDefault!!
            ivPokemonSprite.load(imageUrl) {
                crossfade(true)
            }

            // Format and display the Pokémon's types
            tvPokemonTypes.text = pokemon.types.joinToString(", ") { typeEntry ->
                typeEntry.type.name.replaceFirstChar { it.uppercase() }
            }

            // Format and display some base stats
            val statsText = pokemon.stats
                .filter { it.stat.name in listOf("hp", "attack", "defense") } // Filter for key stats
                .joinToString(" / ") { statEntry ->
                    "${statEntry.stat.name.uppercase()}: ${statEntry.baseStat}"
                }
            tvPokemonStats.text = statsText
        }
    }

    override fun getItemCount() = pokemonDetails.size

    // Optional: A function to update the list if you implement pull-to-refresh later
    fun updateData(newDetails: List<ApiPokemon>) {
        pokemonDetails = newDetails
        notifyDataSetChanged() // In a real app, use DiffUtil for better performance
    }
}
