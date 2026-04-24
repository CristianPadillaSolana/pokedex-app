package com.cpadilso.poxedex.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cpadilso.poxedex.R
import com.cpadilso.poxedex.data.PokeApiService
import com.cpadilso.poxedex.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val apiService = PokeApiService.create()
    private var currentPokemonId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de volver
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Configurar botón de reintentar
        binding.btnRetry.setOnClickListener {
            loadPokemonDetail(currentPokemonId)
        }

        currentPokemonId = intent.getIntExtra("pokemon_id", 1)
        loadPokemonDetail(currentPokemonId)
    }

    private fun loadPokemonDetail(id: Int) {
        currentPokemonId = id
        showLoading()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPokemonDetail(id)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { pokemon ->
                            displayPokemonDetail(pokemon)
                            hideLoading()
                        }
                    } else {
                        showError("Error al cargar detalles")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun displayPokemonDetail(pokemon: com.cpadilso.poxedex.data.PokemonDetail) {
        binding.tvNameDetail.text = pokemon.name.replaceFirstChar { it.uppercase() }
        binding.tvIdDetail.text = "#${pokemon.id.toString().padStart(3, '0')}"
        binding.tvHeight.text = "${pokemon.height / 10.0}m"
        binding.tvWeight.text = "${pokemon.weight / 10.0}kg"

        // Cargar imagen
        val imageUrl = pokemon.sprites.other?.official_artwork?.front_default
            ?: pokemon.sprites.front_default
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivPokemonDetail)

        // Agregar tipos
        binding.llTypes.removeAllViews()
        pokemon.types.forEach { typeSlot ->
            val typeView = TextView(this).apply {
                text = typeSlot.type.name.replaceFirstChar { it.uppercase() }
                textSize = 14f
                setPadding(12, 8, 12, 8)
                setBackgroundResource(R.drawable.bg_type_badge)
                setTextColor(android.graphics.Color.WHITE)
            }
            binding.llTypes.addView(typeView)

            val params = typeView.layoutParams as LinearLayout.LayoutParams
            params.marginEnd = 8
            typeView.layoutParams = params
        }

        // Agregar habilidades
        binding.llAbilities.removeAllViews()
        pokemon.abilities.forEach { abilitySlot ->
            val abilityView = TextView(this).apply {
                text = "• ${abilitySlot.ability.name.replaceFirstChar { it.uppercase() }}"
                textSize = 14f
                setPadding(0, 8, 0, 8)
            }
            binding.llAbilities.addView(abilityView)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.GONE
        binding.scrollView.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.GONE
        binding.btnRetry.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
