package com.example.recipecompanion

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : AppCompatActivity() {
    private val viewModel: RecipeViewModel by viewModels()
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For now, just navigate to edit since you don't have a detail layout
        val recipeId = intent.getIntExtra("recipe_id", -1)

        val intent = Intent(this, EditRecipeActivity::class.java)
        intent.putExtra("recipe_id", recipeId)
        startActivity(intent)
        finish()
    }
}