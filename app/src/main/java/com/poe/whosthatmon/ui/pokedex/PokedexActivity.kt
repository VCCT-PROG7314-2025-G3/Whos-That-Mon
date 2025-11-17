package com.poe.whosthatmon.ui.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
//import androidx.glance.visibility
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
import com.poe.whosthatmon.data.db.AppDatabase
import com.poe.whosthatmon.data.repository.PokemonRepository
import com.poe.whosthatmon.databinding.ActivityPokedexBinding
import com.poe.whosthatmon.ui.AccountScreen
import com.poe.whosthatmon.ui.LoginChoiceActivity
import com.poe.whosthatmon.ui.MainActivity

class PokedexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokedexBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: PokedexViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = PokemonRepository(db.pokemonDao())
        PokedexViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginChoiceActivity::class.java))
            finish()
            return
        }

        setupRecyclerView()
        setupBottomNav()
        observeViewModel()

        // Fetch data for the current user
        viewModel.fetchUnlockedPokemonData(currentUser.uid)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewPokedex.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.unlockedPokemonDetails.observe(this) { unlockedList ->
            if (unlockedList.isEmpty()) {
                binding.tvEmptyPokedex.visibility = View.VISIBLE
                binding.recyclerViewPokedex.visibility = View.GONE
            } else {
                binding.tvEmptyPokedex.visibility = View.GONE
                binding.recyclerViewPokedex.visibility = View.VISIBLE
                binding.recyclerViewPokedex.adapter = PokedexAdapter(unlockedList)
            }
        }
    }

    private fun setupBottomNav() {
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
}
