package com.example.culinarycompanion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
 import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culinarycompanion.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetStarted.setOnTouchListener { v, event ->
            false
        }

        binding.btnGetStarted.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()
            try {
                val intent = Intent(this@WelcomeActivity, RecipeDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        binding.btnGetStarted.setOnLongClickListener {
            Toast.makeText(this, "Long click works!", Toast.LENGTH_SHORT).show()
            true
        }
    }
}
