package com.poe.whosthatmon.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.poe.whosthatmon.R
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Get FCM token (you'll see it in Logcat)


        setContentView(R.layout.activity_splash)

        // Get the current Firebase user
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Decide which activity to start based on whether the user is logged in
        if (currentUser != null) {
            // User is signed in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User is not signed in, go to LoginChoiceActivity
            startActivity(Intent(this, LoginChoiceActivity::class.java))
        }

        // Finish SplashActivity so the user can't navigate back to it
        finish()
    }
}
