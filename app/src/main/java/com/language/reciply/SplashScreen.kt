package com.food.reciply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.language.reciply.SignUp

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val iv_image = findViewById<ImageView>(R.id.iv_splash)
        iv_image.alpha = 0f
        iv_image.animate().setDuration(2000).alpha(1f).withEndAction{
            startActivity(Intent(this, SignUp::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}