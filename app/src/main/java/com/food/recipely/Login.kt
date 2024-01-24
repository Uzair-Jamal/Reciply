package com.food.recipely

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.food.reciply.R
import com.food.reciply.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login<AccessToken> : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val REQ_ONE_TAP = 2
    private lateinit var callBackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

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
        callBackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.googleSignInToken))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this,gso)


        binding.googleSignin.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent,REQ_ONE_TAP)

        }
        binding.facebookSignin.setOnClickListener{

            LoginManager.getInstance().logOut()
            signInWithFacebook()
//            LoginManager.getInstance().registerCallback(callBackManager,
//                object: FacebookCallback<LoginResult> {
//                    override fun onSuccess(result: LoginResult) {
//                        handleFacebookAccessToken(result.accessToken)
//                    }
//
//                    override fun onCancel() {
//                        Toast.makeText(applicationContext,"Facebook login error",Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onError(error: FacebookException?) {
//                        Log.d(TAG,"Facebook Login error: $error")
//                        Toast.makeText(applicationContext, "Facebook login error: ${error?.message}", Toast.LENGTH_LONG).show()
//                    }
//
//                }
//            )
        }
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))

        LoginManager.getInstance().registerCallback(callBackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.accessToken?.let { handleFacebookAccessToken(it) }
                }

                override fun onCancel() {
                    Toast.makeText(applicationContext, "Facebook login canceled", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(applicationContext, "Facebook login error: ${error?.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun handleFacebookAccessToken(accessToken: com.facebook.AccessToken) {
        Log.d(TAG,"handelFacebookAccessToken:$accessToken")

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithCredential: success")
                    val user = auth.currentUser
                    Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    Log.w(TAG,"signInWithCredential: failure", task.exception)
                    Toast.makeText(baseContext,"Authentication failed.",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQ_ONE_TAP){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException){
                Toast.makeText(this,"Google Sign In failed", Toast.LENGTH_LONG).show()
            }
        }
        callBackManager.onActivityResult(requestCode,resultCode,data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign in with Google successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else{
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        val signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId(getString(R.string.googleSignInToken))
//                    .build()
//            ).build()
//
//        when(requestCode){
//            REQ_ONE_TAP -> {
//                try{
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    when {
//                        idToken != null -> {
//                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                            auth.signInWithCredential(firebaseCredential)
//                                .addOnCompleteListener(this) { task ->
//                                    if(task.isSuccessful){
//                                        Log.d(TAG,"signInWithCredential:success")
//                                        val user = auth.currentUser
//                                    }else{
//                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
//                                    }
//                                }
//                        }
//                        else -> {
//                            Log.d(TAG,"No ID token")
//                        }
//                    }
//                }
//            }
//        }
//    }

}