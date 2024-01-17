package com.language.reciply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.food.reciply.R
import com.food.reciply.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.sendBtn.setOnClickListener{
            val user = auth.currentUser

            if(user != null){
            auth.sendPasswordResetEmail(user.email!!)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Password reset email sent", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_LONG).show()
                    }
                }

                }
            else {
                Toast.makeText(this,"No user is current signed in" , Toast.LENGTH_LONG).show()
            }
        }


    }
}