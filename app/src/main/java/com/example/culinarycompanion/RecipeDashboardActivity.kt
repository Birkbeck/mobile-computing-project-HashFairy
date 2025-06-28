package com.example.culinarycompanion

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinarycompanion.databinding.ActivityRecipeDashboardBinding
import com.example.culinarycompanion.databinding.DialogFilterCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class RecipeDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDashboardBinding
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter
    private lateinit var sharedPrefs: SharedPreferences

    private val selectedCategories = mutableSetOf<String>()
    private var allRecipes = listOf<Recipe>()

    companion object {
        private const val PREFS_NAME = "RecipeFilterPrefs"
        private const val KEY_SELECTED_CATEGORIES = "selected_categories"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        loadFilterPreferences()

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        updateFilterIndicator()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter { recipe ->
            // Navigate to recipe detail page
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
            allRecipes = recipes
            filterRecipes()
        }
    }

    private fun setupClickListeners() {
        // Add new recipe
        binding.fabAddRecipe.setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

        // Three dots menu - shows popup menu
        binding.btnMoreOptions.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_dashboard, popup.menu)

        // Update filter menu item to show active filters count
        val filterMenuItem = popup.menu.findItem(R.id.action_filter)
        if (selectedCategories.isNotEmpty()) {
            filterMenuItem.title = "Filter by Category (${selectedCategories.size})"
        }

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_filter -> {
                    showFilterDialog()
                    true
                }
                R.id.action_sort -> {
                    showSortDialog()
                    true
                }
                R.id.action_clear_filters -> {
                    clearAllFilters()
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun showFilterDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogBinding = DialogFilterCategoryBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(dialogBinding.root)

        // Set up checkboxes based on current selection
        dialogBinding.apply {
            checkboxBreakfast.isChecked = selectedCategories.contains("Breakfast")
            checkboxBrunch.isChecked = selectedCategories.contains("Brunch")
            checkboxLunch.isChecked = selectedCategories.contains("Lunch")
            checkboxDinner.isChecked = selectedCategories.contains("Dinner")
            checkboxDessert.isChecked = selectedCategories.contains("Dessert")
            checkboxOther.isChecked = selectedCategories.contains("Other")

            // Clear All button
            btnClearAll.setOnClickListener {
                checkboxBreakfast.isChecked = false
                checkboxBrunch.isChecked = false
                checkboxLunch.isChecked = false
                checkboxDinner.isChecked = false
                checkboxDessert.isChecked = false
                checkboxOther.isChecked = false
            }

            // Apply button
            btnApplyFilter.setOnClickListener {
                selectedCategories.clear()

                if (checkboxBreakfast.isChecked) selectedCategories.add("Breakfast")
                if (checkboxBrunch.isChecked) selectedCategories.add("Brunch")
                if (checkboxLunch.isChecked) selectedCategories.add("Lunch")
                if (checkboxDinner.isChecked) selectedCategories.add("Dinner")
                if (checkboxDessert.isChecked) selectedCategories.add("Dessert")
                if (checkboxOther.isChecked) selectedCategories.add("Other")

                saveFilterPreferences()

                // Apply filter and dismiss dialog
                filterRecipes()
                updateFilterIndicator()
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }

    private fun showSortDialog() {
        val sortOptions = arrayOf("Name (A-Z)", "Name (Z-A)", "Category", "Recently Added")

        android.app.AlertDialog.Builder(this)
            .setTitle("Sort Recipes")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> sortRecipes(SortType.NAME_ASC)
                    1 -> sortRecipes(SortType.NAME_DESC)
                    2 -> sortRecipes(SortType.CATEGORY)
                    3 -> sortRecipes(SortType.RECENT)
                }
            }
            .show()
    }

    private fun sortRecipes(sortType: SortType) {
        val sortedRecipes = when (sortType) {
            SortType.NAME_ASC -> allRecipes.sortedBy { it.name }
            SortType.NAME_DESC -> allRecipes.sortedByDescending { it.name }
            SortType.CATEGORY -> allRecipes.sortedBy { it.category }
            SortType.RECENT -> allRecipes.sortedByDescending { it.id }
        }

        // Apply current filter to sorted list
        if (selectedCategories.isNotEmpty()) {
            val filtered = sortedRecipes.filter { recipe ->
                selectedCategories.contains(recipe.category)
            }
            adapter.submitList(filtered)
        } else {
            adapter.submitList(sortedRecipes)
        }
    }

    /*
  * Display all recipes when no filters are selected
  * Apply category filters to show matching recipes only
  */

    private fun filterRecipes() {
        val filteredRecipes = if (selectedCategories.isEmpty()) {

            allRecipes
        } else {

            allRecipes.filter { recipe ->
                selectedCategories.contains(recipe.category)
            }
        }

        adapter.submitList(filteredRecipes)
        toggleEmptyState(filteredRecipes.isEmpty())
    }

    /*
  * Update empty state message based on active filters
  * Highlight three-dot menu icon when filters are applied
  * Toggle visibility of filter indicator (show/hide)
  * Reset three-dot icon color when filters are cleared
  */
    @SuppressLint("SetTextI18n")
    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerViewRecipes.visibility = View.GONE
            binding.emptyStateLayout.visibility = View.VISIBLE

            if (selectedCategories.isNotEmpty()) {
                binding.emptyStateTitle.text = "No recipes found"
                binding.emptyStateMessage.text = "Try selecting different categories"
            } else {
                binding.emptyStateTitle.text = "No recipes yet"
                binding.emptyStateMessage.text = getString(R.string.tap_the_button_to_add_your_first_recipe)
            }
        } else {
            binding.recyclerViewRecipes.visibility = View.VISIBLE
            binding.emptyStateLayout.visibility = View.GONE
        }
    }

    private fun updateFilterIndicator() {
        if (selectedCategories.isNotEmpty()) {

            binding.filterIndicator.visibility = View.VISIBLE
            binding.filterIndicator.text = "${selectedCategories.size} filter${if (selectedCategories.size > 1) "s" else ""} active"


            binding.btnMoreOptions.setColorFilter(
                ContextCompat.getColor(this, R.color.colorAccent)
            )
        } else {

            binding.filterIndicator.visibility = View.GONE


            binding.btnMoreOptions.clearColorFilter()
        }
    }

    private fun clearAllFilters() {
        selectedCategories.clear()
        saveFilterPreferences()
        filterRecipes()
        updateFilterIndicator()
    }

    private fun saveFilterPreferences() {
        sharedPrefs.edit().apply {
            putStringSet(KEY_SELECTED_CATEGORIES, selectedCategories)
            apply()
        }
    }

    private fun loadFilterPreferences() {
        val savedCategories = sharedPrefs.getStringSet(KEY_SELECTED_CATEGORIES, emptySet())
        savedCategories?.let {
            selectedCategories.clear()
            selectedCategories.addAll(it)
        }
    }

    enum class SortType {
        NAME_ASC, NAME_DESC, CATEGORY, RECENT
    }
}