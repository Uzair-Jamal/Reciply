package com.language.reciply

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.food.reciply.databinding.ActivityForgetPasswordBinding
import com.google.android.play.core.integrity.e
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.sendBtn.setOnClickListener{
            val user = auth.currentUser
            val email = binding.editTextEmail.text.toString().trim()
            val randomCode = generateRandomCode().toString()
            val emailSubject = "Reset password OTP"
            val emailBody = "This is your verification OTP: $randomCode"
            val intent = Intent(Intent.ACTION_SEND)
            if(user == null){

// Store the random code securely, for example, in Firebase Realtime Database or Firestore
                storeCodeInDatabase(email, randomCode)

// Include the random code in the password reset link
                val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl("https://your-app-url.com/resetPassword?email=$email&code=$randomCode")
                    .setHandleCodeInApp(true)
                    .setAndroidPackageName(
                        "com.language.reciply",
                        true, /* installIfNotAvailable */
                        "8" /* minimumVersion */
                    )
                    .build()

                auth.sendPasswordResetEmail(email, actionCodeSettings)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                            // Move to the next activity
                            startActivity(Intent(this, VerifyAccount::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_LONG).show()
                        }
                    }


//                auth.verifyPasswordResetCode(randomCode)
//                    .addOnCompleteListener { task ->
//                        if(task.isSuccessful){
//                            auth.confirmPasswordReset(email,randomCode)
//                                .addOnCompleteListener { resetTask ->
//                                    if (resetTask.isSuccessful){
//                                        startActivity(Intent(this,VerifyAccount::class.java))
//                                        finish()
//                                    }else{
//                                        Toast.makeText(this, "Failed to match the code", Toast.LENGTH_LONG).show()
//                                    }
//                                }
//                        }else{
//                            Toast.makeText(this, "Failed to reset Password", Toast.LENGTH_LONG).show()
//
//                        }
//                    }
//                intent.data = Uri.parse("mailto:")
//                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
//                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
//                intent.putExtra(Intent.EXTRA_TEXT, emailBody)
//                intent.type = "message/rfc822"
//
//                try {
//                    startActivity(Intent.createChooser(intent, "Send Email"))
//                }
//                catch(e: Exception){
//                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
//                }
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