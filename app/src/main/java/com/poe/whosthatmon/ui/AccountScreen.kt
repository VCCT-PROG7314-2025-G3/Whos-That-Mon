package com.poe.whosthatmon.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.poe.whosthatmon.R
import com.poe.whosthatmon.databinding.ActivityAccountScreenBinding
import com.poe.whosthatmon.ui.MainActivity
import com.poe.whosthatmon.ui.pokedex.PokedexActivity

class AccountScreen : AppCompatActivity() {

    private lateinit var binding: ActivityAccountScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, AccountSettings::class.java))
        }

        val bottomNavView = binding.bottomNavContainer.bottomNavigationView

        bottomNavView.selectedItemId = R.id.nav_profile

        bottomNavView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_profile -> true // Already here
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_pokedex -> {
                    startActivity(Intent(this, PokedexActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}