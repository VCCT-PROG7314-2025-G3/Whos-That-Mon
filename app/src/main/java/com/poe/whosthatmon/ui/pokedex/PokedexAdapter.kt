package com.poe.whosthatmon.ui.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.Disposable
import com.poe.whosthatmon.databinding.PokedexItemLayoutBinding
import com.poe.whosthatmon.util.PokemonTypeColorUtil
import com.google.android.material.card.MaterialCardView
// This is the rich Pokemon model from the API, not your local database model
import me.sargunvohra.lib.pokekotlin.model.Pokemon as ApiPokemon


class PokedexAdapter(
    // The adapter now receives a list of our simple PokedexEntry objects
    private val pokedexEntries: List<PokedexEntry>
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
        val entry = pokedexEntries[position]

        with(holder.binding) {
            // Bind the data from our PokedexEntry object to the layout
            tvPokemonName.text = entry.name
            tvPokemonNumber.text = "#${entry.id.toString().padStart(3, '0')}"
            ivPokemonSprite.load(entry.spriteUrl) {
                crossfade(true)
            }
            tvPokemonTypes.text = entry.types
            tvPokemonStats.text = entry.stats
            tvPokemonFlavorText.text = entry.flavorText // Bind the new flavor text

            tvPokemonHeightWeight.text = "Height: ${entry.height} / Weight: ${entry.weight}"

            // --- APPLY THE COLOR ---
            // Get the color for the Pokémon's primary type
            val color = PokemonTypeColorUtil.getColorForType(entry.primaryType)

            // The root of your item layout is a MaterialCardView.
            // We set its background color here.
            (holder.itemView as? MaterialCardView)?.setCardBackgroundColor(color)
        }
    }

    override fun getItemCount() = pokedexEntries.size
}
