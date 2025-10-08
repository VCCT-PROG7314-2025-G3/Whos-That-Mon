package com.poe.whosthatmon.ui.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
import com.poe.whosthatmon.data.LocalPokemonDataSource
import com.poe.whosthatmon.data.api.RetrofitInstance
import com.poe.whosthatmon.data.db.AppDatabase
import com.poe.whosthatmon.data.model.Pokemon
import com.poe.whosthatmon.databinding.ActivityPokedexBinding
import com.poe.whosthatmon.ui.AccountScreen
import com.poe.whosthatmon.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokedexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedexBinding
    private lateinit var adapter: PokedexAdapter
    private val allPokemon = mutableListOf<Pokemon>()
    private val unlockedIds = mutableListOf<Int>()
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        adapter = PokedexAdapter(allPokemon, unlockedIds) { selected ->
            val intent = Intent(this, PokedexDetailActivity::class.java)
            intent.putExtra("POKEMON_ID", selected.id)
            startActivity(intent)
        }

        binding.rvPokedex.layoutManager = LinearLayoutManager(this)
        binding.rvPokedex.adapter = adapter

        loadPokedexFromLocalSource()

        val bottomNavView = binding.bottomNavContainer.bottomNavigationView

        bottomNavView.selectedItemId = R.id.nav_pokedex

        bottomNavView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_pokedex -> true // Already here
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, AccountScreen::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadPokedexFromLocalSource() {
        lifecycleScope.launch(Dispatchers.IO) {
            // 1. Load unlocked IDs from Room
            val db = AppDatabase.getDatabase(applicationContext)
            val unlocked = db.pokemonDao().getUnlockedPokemon("yourUserId")
            unlockedIds.clear()
            unlockedIds.addAll(unlocked.map { it.pokemonId })

            // 2. Load all Pokémon directly from the local data source
            val localPokemonList = LocalPokemonDataSource.getAllPokemon()
            allPokemon.clear()
            allPokemon.addAll(localPokemonList)

            withContext(Dispatchers.Main) {
                adapter.updateData(allPokemon)
            }
        }
    }
}