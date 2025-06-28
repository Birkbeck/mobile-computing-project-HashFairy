package com.example.culinarycompanion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = RecipeDatabase.getDatabase(application).recipeDao()

    val allRecipes: LiveData<List<Recipe>> = dao.getAllRecipes()

    fun getRecipeById(id: Int): LiveData<Recipe> = dao.getRecipeById(id)

    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> = dao.getRecipesByCategory(category)

    fun insertRecipe(recipe: Recipe) = viewModelScope.launch {
        dao.insertRecipe(recipe)
    }

    fun updateRecipe(recipe: Recipe) = viewModelScope.launch {
        dao.updateRecipe(recipe)
    }

    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        dao.deleteRecipe(recipe)
    }
}