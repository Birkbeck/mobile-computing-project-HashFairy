package com.example.recipecompanion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.example.recipecompanion.databinding.ActivityEditRecipeBinding

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getIntExtra("recipe_id", -1)

        setupSpinner()
        loadRecipe(recipeId)
        setupClickListeners()
    }

    private fun setupSpinner() {
        val categories = arrayOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun loadRecipe(id: Int) {
        viewModel.getRecipeById(id).observe(this) { recipe ->
            recipe?.let {
                currentRecipe = it
                populateFields(it)
            }
        }
    }

    private fun populateFields(recipe: Recipe) {
        binding.editRecipeTitle.setText(recipe.name)
        binding.editIngredients.setText(recipe.ingredients)
        binding.editInstructions.setText(recipe.instructions)

        // Set spinner selection
        val categories = arrayOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
        val position = categories.indexOf(recipe.category)
        if (position >= 0) {
            binding.spinnerCategory.setSelection(position)
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdateRecipe.setOnClickListener {
            updateRecipe()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Add delete functionality through long press or menu
        binding.btnUpdateRecipe.setOnLongClickListener {
            showDeleteConfirmation()
            true
        }
    }

    private fun updateRecipe() {
        val name = binding.editRecipeTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val ingredients = binding.editIngredients.text.toString().trim()
        val instructions = binding.editInstructions.text.toString().trim()

        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        currentRecipe?.let { recipe ->
            val updatedRecipe = recipe.copy(
                name = name,
                category = category,
                ingredients = ingredients,
                instructions = instructions
            )

            viewModel.updateRecipe(updatedRecipe)
            Toast.makeText(this, "Recipe updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Delete") { _, _ ->
                deleteRecipe()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteRecipe() {
        currentRecipe?.let { recipe ->
            viewModel.deleteRecipe(recipe)
            Toast.makeText(this, "Recipe deleted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}