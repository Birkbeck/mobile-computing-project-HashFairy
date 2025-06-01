package com.example.recipecompanion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipecompanion.databinding.ActivityAddRecipeBinding

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupClickListeners()
    }

    private fun setupSpinner() {
        val categories = arrayOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun saveRecipe() {
        val name = binding.editRecipeTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val ingredients = binding.editIngredients.text.toString().trim()
        val instructions = binding.editInstructions.text.toString().trim()

        if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = Recipe(
            name = name,
            category = category,
            ingredients = ingredients,
            instructions = instructions
        )

        viewModel.insertRecipe(recipe)
        Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}