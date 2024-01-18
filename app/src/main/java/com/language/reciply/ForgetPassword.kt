package com.language.reciply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.food.reciply.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.sendBtn.setOnClickListener{
            val user = auth.currentUser
            val email = binding.editTextEmail.text.toString()
            val randomCode = generateRandomCode().toString()
            if(user == null){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Password reset email sent", Toast.LENGTH_LONG).show()
                        storeCodeInDatabase(email, randomCode)
                        startActivity(Intent(this,VerifyAccount::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                Toast.makeText(this,"User is currently signed in" , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun storeCodeInDatabase(userEmail: String, randomCode: String) {
        database.child("users").child(userEmail).push().setValue(randomCode)
    }

    private fun generateRandomCode() : Int{
        return Random.nextInt(1000,10000)
    }
}