package com.example.culinarycompanion

import org.junit.Assert.*
import org.junit.Test


/**
 * Unit Test 4: Recipe Data Class Tests
 */
class RecipeTest {

    @Test
    fun `recipe creation with valid data`() {
        val recipe = Recipe(
            id = 1,
            name = "Chocolate Chip Cookies",
            category = "Dessert",
            ingredients = "Flour, Sugar, Butter, Chocolate Chips",
            instructions = "Mix and bake at 375°F for 12 minutes"
        )

        assertEquals(1, recipe.id)
        assertEquals("Chocolate Chip Cookies", recipe.name)
        assertEquals("Dessert", recipe.category)
        assertEquals("Flour, Sugar, Butter, Chocolate Chips", recipe.ingredients)
        assertEquals("Mix and bake at 375°F for 12 minutes", recipe.instructions)
    }

    @Test
    fun `recipe equality check`() {

        val recipe1 = Recipe(1, "Pasta", "Dinner", "Pasta, Sauce", "Cook pasta")
        val recipe2 = Recipe(1, "Pasta", "Dinner", "Pasta, Sauce", "Cook pasta")
        val recipe3 = Recipe(2, "Salad", "Lunch", "Lettuce", "Mix")


        assertEquals(recipe1, recipe2)
        assertNotEquals(recipe1, recipe3)
        assertEquals(recipe1.hashCode(), recipe2.hashCode())
    }

    @Test
    fun `recipe copy functionality`() {

        val original = Recipe(1, "Original", "Breakfast", "Eggs", "Cook")


        val modified = original.copy(name = "Modified", ingredients = "Eggs, Bacon")


        assertEquals(original.id, modified.id)
        assertEquals("Modified", modified.name)
        assertEquals("Eggs, Bacon", modified.ingredients)
        assertEquals(original.category, modified.category)
        assertEquals(original.instructions, modified.instructions)
    }

    @Test
    fun `recipe with auto-generated id defaults to 0`() {

        val recipe = Recipe(
            name = "New Recipe",
            category = "Lunch",
            ingredients = "Ingredients",
            instructions = "Instructions"
        )


        assertEquals(0, recipe.id)
    }
}