package com.poe.whosthatmon.ui

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
import com.poe.whosthatmon.data.db.AppDatabase
import com.poe.whosthatmon.data.repository.PokemonRepository
import com.poe.whosthatmon.databinding.ActivityMainBinding
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
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Redirecting to login...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginChoiceActivity::class.java))
            finish()
            return
        }

        setupUI()
        observeViewModel()
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

                // Unlock the Pokémon for the current user
                viewModel.onCorrectGuess(auth.currentUser!!.uid, currentPokemonId!!)
            } else {
                Toast.makeText(this, "Not quite! Try again!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNextPokemon.setOnClickListener {
            resetForNextPokemon()
        }

        val bottomNavView = binding.bottomNavContainer.bottomNavigationView
        bottomNavView.selectedItemId = R.id.nav_home
        bottomNavView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> true
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

    private fun observeViewModel() {
        // This observes the master list from the database.
        viewModel.allLocalPokemon.observe(this) { pokemonList ->
            if (pokemonList.isNotEmpty() && viewModel.randomPokemon.value == null) {
                // If the list is ready and we haven't picked a Pokémon yet, pick one.
                viewModel.fetchRandomPokemonFromLocal()
                // Enable the UI once the list is confirmed to be ready
                binding.etGuess.isEnabled = true
                binding.btnSubmitGuess.isEnabled = true
            } else if (pokemonList.isEmpty()) {
                // Show a loading/preparation indicator and disable UI while the DB is populating
                binding.etGuess.hint = "Loading Pokémon..."
                binding.etGuess.isEnabled = false
                binding.btnSubmitGuess.isEnabled = false
            }
        }

        // This observes the single random Pokémon chosen for the current round.
        viewModel.randomPokemon.observe(this) { localPokemon ->
            // Use the LocalPokemon object directly from the database for ALL data.
            currentPokemonName = localPokemon.name
            currentPokemonId = localPokemon.id

            // Use the spriteUrl that we saved in our local database.
            binding.ivPokemon.load(localPokemon.spriteUrl) {
                listener(onStart = { applySilhouette() })
                error(R.drawable.pokebal_bg)
            }
        }

        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun resetForNextPokemon() {
        revealed = false
        binding.etGuess.text.clear()
        binding.etGuess.isEnabled = true
        binding.btnNextPokemon.visibility = View.GONE
        viewModel.fetchRandomPokemonFromLocal()
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

