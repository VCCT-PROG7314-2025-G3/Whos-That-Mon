package com.poe.whosthatmon.ui

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.poe.whosthatmon.data.db.AppDatabase
import com.poe.whosthatmon.data.repository.PokemonRepository
import com.poe.whosthatmon.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(db.pokemonDao())
        MainViewModelFactory(repository)
    }

    private lateinit var auth: FirebaseAuth
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
            // Redirect to the login choice screen
            startActivity(Intent(this, LoginChoiceActivity::class.java))
            finish()
            return
        }

        setupUI()
        observeViewModel()

        // Load the first Pokémon if it hasn't been loaded already
        if (savedInstanceState == null) {
            viewModel.fetchRandomPokemon()
        }
    }

    private fun setupUI() {
        binding.btnSubmitGuess.setOnClickListener {
            val guess = binding.etGuess.text.toString().trim().lowercase()
            val correct = currentPokemonName?.lowercase()

            if (guess.isEmpty()) {
                Toast.makeText(this, "Enter a Pokémon name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (guess.equals(correct, ignoreCase = true)) {
                revealPokemon()
                Toast.makeText(this, "It's ${currentPokemonName!!.uppercase()}! You caught it!", Toast.LENGTH_LONG).show()
                binding.btnNextPokemon.visibility = View.VISIBLE
                binding.etGuess.isEnabled = false // Disable input after correct guess
            } else {
                Toast.makeText(this, "Not quite! Try again!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNextPokemon.setOnClickListener {
            revealed = false
            binding.etGuess.text.clear()
            binding.etGuess.isEnabled = true // Re-enable input
            binding.btnNextPokemon.visibility = View.GONE
            viewModel.fetchRandomPokemon()
        }
    }

    private fun observeViewModel() {
        viewModel.pokemon.observe(this) { pokemon ->
            currentPokemonName = pokemon.name
            currentPokemonId = pokemon.id
            // Construct the image URL using the Pokémon's ID
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            binding.ivPokemon.load(imageUrl) {
                // This ensures the silhouette is applied before the image is displayed
                listener(
                    onStart = {
                        applySilhouette()
                    }
                )
                error(R.drawable.pokebal_bg)
            }
        }

        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun applySilhouette() {
        // Creates a filter that makes the image completely black but preserves transparency
        val matrix = ColorMatrix(
            floatArrayOf(
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        binding.ivPokemon.colorFilter = ColorMatrixColorFilter(matrix)
        revealed = false
    }

    private fun revealPokemon() {
        binding.ivPokemon.colorFilter = null
        revealed = true
    }
}