package com.food.recipely

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.food.reciply.R
import com.food.reciply.databinding.ActivityInterestBinding

class Interest : AppCompatActivity() {
    private lateinit var binding: ActivityInterestBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.interest1.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                it.setBackgroundResource(R.drawable.rounded_image)
            }else{
                it.setBackgroundResource(0)
            }
        }
        binding.interestBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}