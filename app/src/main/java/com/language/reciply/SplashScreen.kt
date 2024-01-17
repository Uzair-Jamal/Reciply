package com.food.reciply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.language.reciply.Login
import com.language.reciply.SignUp

class SplashScreen : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = Firebase.auth
        val iv_image = findViewById<ImageView>(R.id.iv_splash)
        iv_image.alpha = 0f
        iv_image.animate().setDuration(2000).alpha(1f).withEndAction {
            if (auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                Log.d("User", "User: ${auth.currentUser}")
            } else {
                startActivity(Intent(this, Login::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            finish()
        }
    }
}