package com.example.recipecompanion

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipecompanion.databinding.ActivityRecipeDashboardBinding

class RecipeDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDashboardBinding
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    //private fun setupToolbar() {
        //setSupportActionBar(binding.toolbar)
        //supportActionBar?.title = "My Recipes"
    //}

    private fun setupRecyclerView() {
        adapter = RecipeAdapter { recipe ->
            // Navigate to recipe detail
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipe_id", recipe.id)
            startActivity(intent)
        }

        binding.recyclerViewRecipes.apply {
            layoutManager = LinearLayoutManager(this@RecipeDashboardActivity)
            adapter = this@RecipeDashboardActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.allRecipes.observe(this) { recipes ->
            adapter.submitList(recipes)
            updateRecipeCount(recipes.size)
            toggleEmptyState(recipes.isEmpty())
        }
    }

    private fun setupClickListeners() {
        // Add new recipe
        binding.fabAddRecipe.setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

    }

    private fun updateRecipeCount(count: Int) {
        binding.textRecipeCount.text = "$count recipes"
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerViewRecipes.visibility = android.view.View.GONE
            binding.emptyStateLayout.visibility = android.view.View.VISIBLE
        } else {
            binding.recyclerViewRecipes.visibility = android.view.View.VISIBLE
            binding.emptyStateLayout.visibility = android.view.View.GONE
        }
    }
}
