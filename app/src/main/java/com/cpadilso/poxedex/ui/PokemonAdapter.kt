package com.cpadilso.poxedex.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cpadilso.poxedex.data.PokemonBasic
import com.cpadilso.poxedex.databinding.ItemPokemonBinding

class PokemonAdapter(
    private val onPokemonClick: (PokemonBasic) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private val pokemonList = mutableListOf<PokemonBasic>()

    fun addPokemon(newPokemon: List<PokemonBasic>) {
        val startPosition = pokemonList.size
        pokemonList.addAll(newPokemon)
        notifyItemRangeInserted(startPosition, newPokemon.size)
    }

    fun clearPokemon() {
        pokemonList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount() = pokemonList.size

    inner class PokemonViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonBasic) {
            binding.tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.tvPokemonId.text = "#${pokemon.id.toString().padStart(3, '0')}"

            Glide.with(binding.root.context)
                .load(pokemon.imageUrl)
                .into(binding.ivPokemon)

            binding.root.setOnClickListener {
                onPokemonClick(pokemon)
            }
        }
    }
}
