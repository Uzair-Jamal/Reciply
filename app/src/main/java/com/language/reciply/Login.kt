package com.language.reciply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.food.reciply.MainActivity
import com.food.reciply.R
import com.food.reciply.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.newUser.setOnClickListener{
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailAddress.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            } else {
                // All checks passed, proceed with Firebase authentication
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Authentication successful, navigate to the main activity
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Authentication failed
                            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.forgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPassword::class.java))
            finish()
        }
    }
}