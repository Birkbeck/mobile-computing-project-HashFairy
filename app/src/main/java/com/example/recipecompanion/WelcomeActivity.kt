package com.example.recipecompanion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipecompanion.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        private const val TAG = "WelcomeActivity"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate started")

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Layout inflated")

        // Check if button exists
        if (false) {
            Log.e(TAG, "Button is null!")
            return
        }

        Log.d(TAG, "Button found: ${binding.btnGetStarted}")

        // Set multiple listeners to debug
        binding.btnGetStarted.setOnTouchListener { v, event ->
            Log.d(TAG, "Button touched: $event")
            false // Return false to allow click event to propagate
        }

        binding.btnGetStarted.setOnClickListener {
            Log.d(TAG, "=== BUTTON CLICKED ===")
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()

            try {
                // Test if we can even create a simple intent
                Log.d(TAG, "Creating intent...")
                val intent = Intent(this@WelcomeActivity, RecipeDashboardActivity::class.java)
                Log.d(TAG, "Intent created: $intent")

                // Try to start the activity
                Log.d(TAG, "Calling startActivity...")
                startActivity(intent)
                Log.d(TAG, "startActivity called successfully")

                // Finish this activity
                Log.d(TAG, "Calling finish...")
                finish()
                Log.d(TAG, "finish called successfully")

            } catch (e: Exception) {
                Log.e(TAG, "ERROR starting activity: ${e.message}", e)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        // Also add a long click listener as a test
        binding.btnGetStarted.setOnLongClickListener {
            Log.d(TAG, "Button long clicked!")
            Toast.makeText(this, "Long click works!", Toast.LENGTH_SHORT).show()
            true
        }

        // Log button properties
        Log.d(TAG, "Button visibility: ${binding.btnGetStarted.visibility}")
        Log.d(TAG, "Button is enabled: ${binding.btnGetStarted.isEnabled}")
        Log.d(TAG, "Button is clickable: ${binding.btnGetStarted.isClickable}")

        Log.d(TAG, "onCreate completed")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }
}