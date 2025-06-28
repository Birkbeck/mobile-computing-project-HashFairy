package com.example.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.culinarycompanion.databinding.ActivityRecipeDetailBinding

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var currentRecipe: Recipe? = null
    private var recipeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeId = intent.getIntExtra("recipe_id", -1)

        if (recipeId == -1) {
            Toast.makeText(this, "Error: Recipe not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupClickListeners()
        loadRecipeDetails()
    }

    private fun loadRecipeDetails() {
        viewModel.getRecipeById(recipeId).observe(this) { recipe ->
            recipe?.let {
                currentRecipe = it
                displayRecipe(it)
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        binding.apply {
            textRecipeTitle.text = recipe.name
            textCategoryBadge.text = recipe.category
            textIngredients.text = recipe.ingredients
            textInstructions.text = recipe.instructions

            textIngredients.movementMethod = android.text.method.ScrollingMovementMethod()
            textInstructions.movementMethod = android.text.method.ScrollingMovementMethod()


        }
    }

    private fun setupClickListeners() {
        // Back button - returns to My Recipe dashboard
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Edit button - navigates to edit page
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("recipe_id", recipeId)
            startActivity(intent)
        }

        // Delete button - shows confirmation dialog
        binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete \"${currentRecipe?.name}\"? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteRecipe()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(getColor(R.color.colorAccent))
                    getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(getColor(R.color.textSecondary))
                }
            }
            .show()
    }

    private fun deleteRecipe() {
        currentRecipe?.let { recipe ->
            viewModel.deleteRecipe(recipe)
            Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}