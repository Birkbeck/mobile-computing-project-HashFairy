
package com.example.recipecompanion

/**
 * Data class representing a Recipe
 */
data class Recipe(
    val id: Long,
    val title: String,
    val category: String,
    val ingredients: String = "",
    val instructions: String = "",
)