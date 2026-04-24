package com.cpadilso.poxedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpadilso.poxedex.data.PokeApiService
import com.cpadilso.poxedex.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PokemonAdapter
    private val apiService = PokeApiService.create()

    private var currentOffset = 0
    private val limit = 20
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadPokemon()
    }

    private fun setupRecyclerView() {
        adapter = PokemonAdapter { pokemon ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("pokemon_id", pokemon.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Paginación
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                    loadPokemon()
                }
            }
        })
    }

    private fun loadPokemon() {
        if (isLoading) return
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPokemonList(limit, currentOffset)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            adapter.addPokemon(data.results)
                            currentOffset += limit
                        }
                    } else {
                        android.widget.Toast.makeText(
                            this@MainActivity,
                            "Error al cargar pokémon",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.message}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    isLoading = false
                }
            }
        }
    }
}
