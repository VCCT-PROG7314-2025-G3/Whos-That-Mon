package com.poe.whosthatmon.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.poe.whosthatmon.R
import com.poe.whosthatmon.databinding.ActivityAccountSettingsBinding
import java.util.*

class AccountSettings : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingsBinding
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        notificationHelper = NotificationHelper(this)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadSettings()

        // Sync
        binding.switchSync.setOnCheckedChangeListener { _, isChecked ->
            saveBoolean("sync_enabled", isChecked)
        }

        // Biometrics
        binding.switchBiometrics.setOnCheckedChangeListener { _, isChecked ->
            saveBoolean("biometrics_enabled", isChecked)
        }

        // Language
        binding.itemLanguage.setOnClickListener {
            showLanguageDialog()
        }

        // Notifications
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            saveBoolean("notifications_enabled", isChecked)
            if (isChecked) {
                // Send a test notification to confirm it works
                notificationHelper.showNotification(
                    getString(R.string.notifications_enabled_title), // Use string resource
                    getString(R.string.notifications_enabled_message) // Use string resource
                )
            }
        }

        // Logout
        binding.itemLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    // Change this to your actual login screen
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity() // Closes all activities
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun loadSettings() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)

        binding.switchSync.isChecked = prefs.getBoolean("sync_enabled", true)
        binding.switchBiometrics.isChecked = prefs.getBoolean("biometrics_enabled", true)
        binding.switchNotifications.isChecked = prefs.getBoolean("notifications_enabled", true)

        val savedLang = prefs.getString("app_language", "en") ?: "en"
        updateLanguageText(savedLang)
        applyLanguage(savedLang)
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Afrikaans", "Zulu", "Xhosa")
        AlertDialog.Builder(this)
            .setTitle("Choose Language")
            .setItems(languages) { _, which ->
                val code = when (which) {
                    0 -> "en"
                    1 -> "af"
                    2 -> "zu"
                    3 -> "xh"
                    else -> "en"
                }
                saveString("app_language", code)
                updateLanguageText(code)
                applyLanguage(code)
                recreate() // Instantly refreshes app
            }
            .show()
    }

    private fun updateLanguageText(code: String) {
        binding.tvCurrentLanguage.text = when (code) {
            "af" -> "Afrikaans"
            "zu" -> "Zulu"
            "xh" -> "Xhosa"
            else -> "English"
        }
    }

    private fun applyLanguage(code: String) {
        val locale = when (code) {
            "af" -> Locale("af")
            "zu" -> Locale("zu")
            "xh" -> Locale("xh")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    private fun saveString(key: String, value: String) {
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }
}