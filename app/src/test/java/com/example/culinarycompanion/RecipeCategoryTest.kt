package com.example.culinarycompanion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test 2: Recipe Category and Filtering Logic Tests
 */
class RecipeCategoryTest {

    private val validCategories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Dessert", "Other")

    @Test
    fun `all recipe categories are valid`() {

        val recipes = listOf(
            Recipe(1, "Test1", "Breakfast", "Ing", "Inst"),
            Recipe(2, "Test2", "Lunch", "Ing", "Inst"),
            Recipe(3, "Test3", "Dinner", "Ing", "Inst"),
            Recipe(4, "Test4", "Dessert", "Ing", "Inst")
        )

        recipes.forEach { recipe ->
            assertTrue(
                "Category ${recipe.category} should be valid",
                validCategories.contains(recipe.category)
            )
        }
    }

    @Test
    fun `filter with empty category returns all recipes`() {

        val recipes = listOf(
            Recipe(1, "Recipe1", "Breakfast", "Ing1", "Inst1"),
            Recipe(2, "Recipe2", "Lunch", "Ing2", "Inst2"),
            Recipe(3, "Recipe3", "Dinner", "Ing3", "Inst3")
        )


        val filtered = filterRecipesByCategory(recipes, "")


        assertEquals(recipes.size, filtered.size)
        assertEquals(recipes, filtered)
    }

    @Test
    fun `filter with multiple categories`() {
        // Given
        val recipes = listOf(
            Recipe(1, "Pancakes", "Breakfast", "Flour", "Cook"),
            Recipe(2, "Sandwich", "Lunch", "Bread", "Make"),
            Recipe(3, "Eggs", "Breakfast", "Eggs", "Fry"),
            Recipe(4, "Pasta", "Dinner", "Pasta", "Boil"),
            Recipe(5, "Cake", "Dessert", "Flour", "Bake")
        )
        val selectedCategories = setOf("Breakfast", "Dessert")

        val filtered = recipes.filter { selectedCategories.contains(it.category) }


        assertEquals(3, filtered.size)
        assertTrue(filtered.all {
            it.category == "Breakfast" || it.category == "Dessert"
        })
    }

    @Test
    fun `sort recipes by category groups them correctly`() {

        val recipes = listOf(
            Recipe(1, "Pasta", "Dinner", "Pasta", "Cook"),
            Recipe(2, "Pancakes", "Breakfast", "Flour", "Cook"),
            Recipe(3, "Salad", "Lunch", "Lettuce", "Mix"),
            Recipe(4, "Eggs", "Breakfast", "Eggs", "Fry"),
            Recipe(5, "Steak", "Dinner", "Beef", "Grill")
        )


        val sortedByCategory = recipes.sortedBy { it.category }

        assertEquals("Breakfast", sortedByCategory[0].category)
        assertEquals("Breakfast", sortedByCategory[1].category)
        assertEquals("Dinner", sortedByCategory[2].category)
        assertEquals("Dinner", sortedByCategory[3].category)
        assertEquals("Lunch", sortedByCategory[4].category)
    }

    // Helper function
    private fun filterRecipesByCategory(recipes: List<Recipe>, category: String): List<Recipe> {
        return if (category.isEmpty()) {
            recipes
        } else {
            recipes.filter { it.category == category }
        }
    }
}