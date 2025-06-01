package com.example.recipecompanion

import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipecompanion.databinding.ActivityCategoryFilterBinding

class CategoryFilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryFilterBinding
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter
    private val categoryCheckboxes = mutableListOf<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupCategoryCheckboxes()
        setupClickListeners()
        loadAllRecipes()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Filter Recipes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        // Note: Your layout doesn't have recyclerViewRecipes, so we'll need to add filtering logic
        // For now, this will work if you add a RecyclerView to your layout
        adapter = RecipeAdapter { recipe ->
            // Handle recipe click
        }
    }

    private fun setupCategoryCheckboxes() {
        val categories = arrayOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snack")

        for (category in categories) {
            val checkBox = CheckBox(this)
            checkBox.text = category
            checkBox.textSize = 16f
            checkBox.setPadding(0, 16, 0, 16)

            categoryCheckboxes.add(checkBox)
            binding.categoryContainer.addView(checkBox)
        }
    }

    private fun setupClickListeners() {
        binding.buttonClearAll.setOnClickListener {
            clearAllFilters()
        }

        binding.buttonApply.setOnClickListener {
            applyFilters()
        }
    }

    private fun loadAllRecipes() {
        viewModel.allRecipes.observe(this) { recipes ->
            // Update UI with all recipes
        }
    }

    private fun clearAllFilters() {
        categoryCheckboxes.forEach { it.isChecked = false }
    }

    private fun applyFilters() {
        val selectedCategories = categoryCheckboxes
            .filter { it.isChecked }
            .map { it.text.toString() }

        if (selectedCategories.isEmpty()) {
            // Show all recipes
            loadAllRecipes()
        } else {
            // Filter recipes by selected categories
            // You can implement this based on your needs
        }

        finish() // Return to previous screen
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
