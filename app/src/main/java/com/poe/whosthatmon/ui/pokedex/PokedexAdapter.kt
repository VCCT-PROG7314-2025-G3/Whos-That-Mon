package com.poe.whosthatmon.ui.pokedex

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.poe.whosthatmon.R
import com.poe.whosthatmon.data.model.Pokemon
import com.poe.whosthatmon.databinding.PokedexItemLayoutBinding

class PokedexAdapter (
    private var pokemons: List<Pokemon>,
    private val unlockedIds: List<Int>,
    private val onItemClick: (Pokemon) -> Unit
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
            val pokemon = pokemons[position]
            val isUnlocked = unlockedIds.contains(pokemon.id)

            with(holder.binding) {
                if (isUnlocked) {
                    ivPokemonSprite.load(pokemon.imageUrl)
                    tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
                    tvPokemonNumber.text = "No. ${pokemon.id}"
                    ivPokemonSprite.colorFilter = null
                    holder.itemView.alpha = 1.0f
                } else {
                    ivPokemonSprite.load(pokemon.imageUrl)
                    val matrix = ColorMatrix(
                        floatArrayOf(
                            0f, 0f, 0f, 0f, 0f,
                            0f, 0f, 0f, 0f, 0f,
                            0f, 0f, 0f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                    val filter = ColorMatrixColorFilter(matrix)
                    ivPokemonSprite.colorFilter = filter

                    tvPokemonName.text = "???"
                    tvPokemonNumber.text = "No. ${pokemon.id.toString().padStart(3, '0')}"
                    holder.itemView.alpha = 0.5f
                }

                root.setOnClickListener {
                    if (isUnlocked) onItemClick(pokemon)
                }
            }
        }

        override fun getItemCount() = pokemons.size

        fun updateData(newList: List<Pokemon>) {
            pokemons = newList
            notifyDataSetChanged()
        }
}