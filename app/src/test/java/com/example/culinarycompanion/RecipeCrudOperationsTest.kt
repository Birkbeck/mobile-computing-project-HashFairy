package com.example.culinarycompanion

import org.junit.Assert.*
import org.junit.Test
import org.junit.Before


/**
 * Unit Test 3
 * Unit Tests for Recipe CRUD Operations match Activity implementations
 * Tests the business logic from AddRecipeActivity, EditRecipeActivity, and RecipeDetailActivity
 */
class RecipeCrudOperationsTest {


    private lateinit var recipeCategories: Array<String>

    @Before
    fun setup() {
        recipeCategories = arrayOf("Breakfast", "Brunch", "Lunch", "Dinner", "Dessert", "Other")
    }



    /**
     * Tests the validation logic from AddRecipeActivity.saveRecipe()
     */

    @Test
    fun `add recipe validation requires all fields`() {

        assertFalse(isValidForSave("", "Ingredients", "Instructions"))
        assertFalse(isValidForSave("Name", "", "Instructions"))
        assertFalse(isValidForSave("Name", "Ingredients", ""))
        assertTrue(isValidForSave("Name", "Ingredients", "Instructions"))
        assertFalse(isValidForSave("   ", "Ingredients", "Instructions"))
    }

    /**
     * Tests that input is trimmed before saving
     */
    @Test
    fun `add recipe trims whitespace from all fields`() {
        val recipe = createRecipeFromInput(
            nameInput = "  Chocolate Cake  ",
            categoryInput = "Dessert",
            ingredientsInput = "  Flour, Sugar  ",
            instructionsInput = "  Mix and bake  "
        )

        assertEquals("Chocolate Cake", recipe.name)
        assertEquals("Flour, Sugar", recipe.ingredients)
        assertEquals("Mix and bake", recipe.instructions)
    }

    /**
     * Tests edge case: special characters and unicode
     */

    @Test
    fun `add recipe handles special characters`() {
        val recipe = createRecipeFromInput(
            nameInput = "Mom's \"Special\" Recipe & Crème Brûlée",
            categoryInput = "Dessert",
            ingredientsInput = "1/2 cup sugar, café au lait",
            instructionsInput = "Heat to 350°F. Bon appétit!"
        )

        assertTrue(recipe.name.contains("\""))
        assertTrue(recipe.name.contains("&"))
        assertTrue(recipe.name.contains("û"))
        assertTrue(recipe.instructions.contains("°"))
    }

    /**
     * Tests the category list from AddRecipeActivity
     */
    @Test
    fun `add recipe uses correct categories`() {
        assertEquals(6, recipeCategories.size)
        assertTrue(recipeCategories.contains("Breakfast"))
        assertTrue(recipeCategories.contains("Brunch"))
        assertTrue(recipeCategories.contains("Other"))
    }

    // ===== EditRecipeActivity Tests (3 tests) =====

    /**
     * Tests the validation logic from EditRecipeActivity.updateRecipe() line 76
     */
    @Test
    fun `edit recipe validation same as add`() {
        assertFalse(isValidForUpdate("", "Ingredients", "Instructions"))
        assertTrue(isValidForUpdate("Name", "Ingredients", "Instructions"))
    }

    /**
     * Tests category position finding from EditRecipeActivity.populateFields()
     */
    @Test
    fun `edit recipe finds category position`() {
        assertEquals(0, findCategoryPosition("Breakfast"))
        assertEquals(4, findCategoryPosition("Dessert"))
        assertEquals(-1, findCategoryPosition("Invalid"))
    }

    /**
     * Tests recipe update maintains ID but changes other fields
     */

    @Test
    fun `edit recipe updates correctly`() {
        val original = Recipe(42, "Original", "Breakfast", "Eggs", "Fry")

        val updated = original.copy(
            name = "Updated Name",
            category = "Brunch",
            ingredients = "Eggs, Bacon",
            instructions = "Fry with bacon"
        )

        assertEquals(42, updated.id)
        assertEquals("Updated Name", updated.name)
        assertNotEquals(original.name, updated.name)
    }

    // ===== RecipeDetailActivity Tests (2 tests) =====

    /**
     * Tests recipe ID validation from RecipeDetailActivity
     */
    @Test
    fun `detail activity validates recipe id`() {
        assertTrue(isValidRecipeId(1))
        assertTrue(isValidRecipeId(100))
        assertFalse(isValidRecipeId(-1))
    }

    /**
     * Tests delete confirmation message generation
     */
    @Test
    fun `delete confirmation message includes recipe name`() {
        val recipe = Recipe(1, "Chocolate Cake", "Dessert", "Ing", "Inst")
        val message = getDeleteConfirmationMessage(recipe)

        assertTrue(message.contains("Chocolate Cake"))
        assertTrue(message.contains("Are you sure"))
    }

    // ===== Helper Functions that mirror Activity logic =====

    private fun isValidForSave(name: String, ingredients: String, instructions: String): Boolean {
        val trimmedName = name.trim()
        val trimmedIngredients = ingredients.trim()
        val trimmedInstructions = instructions.trim()

        return !(trimmedName.isEmpty() || trimmedIngredients.isEmpty() || trimmedInstructions.isEmpty())
    }

    private fun isValidForUpdate(name: String, ingredients: String, instructions: String): Boolean {
        return isValidForSave(name, ingredients, instructions)
    }

    private fun createRecipeFromInput(
        nameInput: String,
        categoryInput: String,
        ingredientsInput: String,
        instructionsInput: String
    ): Recipe {
        return Recipe(
            name = nameInput.trim(),
            category = categoryInput,
            ingredients = ingredientsInput.trim(),
            instructions = instructionsInput.trim()
        )
    }

    private fun findCategoryPosition(category: String): Int {
        return recipeCategories.indexOf(category)
    }

    private fun isValidRecipeId(id: Int): Boolean {
        return id != -1
    }

    private fun getDeleteConfirmationMessage(recipe: Recipe): String {
        // Mirrors RecipeDetailActivity.showDeleteConfirmationDialog() message
        return "Are you sure you want to delete \"${recipe.name}\"? This action cannot be undone."
    }
}