package com.poe.whosthatmon.ui

import android.widget.Toast
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
import com.poe.whosthatmon.data.db.AppDatabase
import com.poe.whosthatmon.data.repository.PokemonRepository
import com.poe.whosthatmon.databinding.ActivityMainBinding
import com.poe.whosthatmon.databinding.IncludeBottomNavBinding
import com.poe.whosthatmon.ui.pokedex.PokedexActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: MainViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(db.pokemonDao())
        MainViewModelFactory(repository)
    }

    private var currentPokemonName: String? = null
    private var currentPokemonId: Int? = null
    private var revealed = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            Toast.makeText(this, "Redirecting to login...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginChoiceActivity::class.java))
            finish()
            return
        }

        setupUI()
        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.fetchRandomPokemon()
        }

        val bottomNavView = binding.bottomNavContainer.bottomNavigationView

        bottomNavView.selectedItemId = R.id.nav_home

        bottomNavView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> true // Already here
                R.id.nav_pokedex -> {
                    startActivity(Intent(this, PokedexActivity::class.java))
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

    private fun setupUI() {
        binding.btnSubmitGuess.setOnClickListener {
            val guess = binding.etGuess.text.toString().trim()
            if (guess.isEmpty()) {
                Toast.makeText(this, "Enter a Pokémon name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (guess.equals(currentPokemonName, ignoreCase = true)) {
                revealPokemon()
                Toast.makeText(this, "It's ${currentPokemonName!!.uppercase()}! You caught it!", Toast.LENGTH_LONG).show()
                binding.btnNextPokemon.visibility = View.VISIBLE
                binding.etGuess.isEnabled = false

            } else {
                Toast.makeText(this, "Not quite! Try again!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNextPokemon.setOnClickListener {
            revealed = false
            binding.etGuess.text.clear()
            binding.etGuess.isEnabled = true
            binding.btnNextPokemon.visibility = View.GONE
            viewModel.fetchRandomPokemon()
        }
    }

    private fun observeViewModel() {
        viewModel.pokemon.observe(this) { pokemon ->
            currentPokemonName = pokemon.name
            currentPokemonId = pokemon.id
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            binding.ivPokemon.load(imageUrl) {
                listener(onStart = { applySilhouette() })
                error(R.drawable.pokebal_bg)
            }
        }
        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun applySilhouette() {
        val matrix = ColorMatrix(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f))
        binding.ivPokemon.colorFilter = ColorMatrixColorFilter(matrix)
        revealed = false
    }

    private fun revealPokemon() {
        binding.ivPokemon.colorFilter = null
        revealed = true
    }
}
