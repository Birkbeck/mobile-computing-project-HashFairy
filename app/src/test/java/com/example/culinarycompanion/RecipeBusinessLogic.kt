package com.example.culinarycompanion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeBusinessLogic {


    /**
     * Unit Test 1
     * Additional tests for CRUD operation business logic
     * Tests the complete flow of adding a recipe
     * Simulates the entire AddRecipeActivity.saveRecipe() process
     */


        @Test
        fun `add recipe flow with valid and invalid data`() {
            val validInput = RecipeInput(
                name = "  New Recipe  ",
                category = "Dinner",
                ingredients = "  Ingredient 1, Ingredient 2  ",
                instructions = "  Step 1: Do this\nStep 2: Do that  "
            )

            val validResult = processAddRecipe(validInput)

            assertTrue(validResult.success)
            assertEquals("Recipe saved!", validResult.message)
            assertEquals("New Recipe", validResult.recipe?.name)

            // Test invalid cases
            val invalidInputs = listOf(
                RecipeInput("", "Lunch", "Ing", "Inst"),
                RecipeInput("Name", "Lunch", "", "Inst"),
                RecipeInput("   ", "Lunch", "Ing", "Inst")
            )

            invalidInputs.forEach { input ->
                val result = processAddRecipe(input)
                assertFalse(result.success)
                assertEquals("Please fill all fields", result.message)
            }
        }

        /**
         * Tests category validation
         */
        @Test
        fun `validate category from allowed list`() {
            val validCategories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Dessert", "Other")

            assertTrue(isValidCategory("Breakfast", validCategories))
            assertFalse(isValidCategory("Snack", validCategories))
            assertFalse(isValidCategory("breakfast", validCategories))
        }

        /**
         * Tests detecting changes in edit flow
         */
        @Test
        fun `detect recipe changes for edit`() {
            val original = Recipe(1, "Original", "Lunch", "Ing1", "Inst1")

            assertTrue(hasRecipeChanged(original, "Modified", "Lunch", "Ing1", "Inst1"))
            assertTrue(hasRecipeChanged(original, "Original", "Dinner", "Ing1", "Inst1"))
            assertFalse(hasRecipeChanged(original, "Original", "Lunch", "Ing1", "Inst1"))
        }

        /**
         * Tests input sanitization
         */
        @Test
        fun `sanitize user input`() {
            assertEquals("Normal Recipe", "  Normal Recipe  ".trim())
            assertEquals("", "   ".trim())
            assertEquals("Recipe\nWith\nNewlines", "Recipe\nWith\nNewlines".trim())
        }

        // Helper classes and functions

        data class RecipeInput(
            val name: String,
            val category: String,
            val ingredients: String,
            val instructions: String
        )

        data class ProcessResult(
            val success: Boolean,
            val message: String,
            val recipe: Recipe?
        )

        private fun processAddRecipe(input: RecipeInput): ProcessResult {
            // Simulates AddRecipeActivity.saveRecipe() logic
            val name = input.name.trim()
            val ingredients = input.ingredients.trim()
            val instructions = input.instructions.trim()

            if (name.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                return ProcessResult(false, "Please fill all fields", null)
            }

            val recipe = Recipe(
                name = name,
                category = input.category,
                ingredients = ingredients,
                instructions = instructions
            )

            return ProcessResult(true, "Recipe saved!", recipe)
        }

        private fun isValidCategory(category: String, validCategories: List<String>): Boolean {
            return validCategories.contains(category)
        }

        private fun hasRecipeChanged(
            original: Recipe,
            newName: String,
            newCategory: String,
            newIngredients: String,
            newInstructions: String
        ): Boolean {
            return original.name != newName ||
                    original.category != newCategory ||
                    original.ingredients != newIngredients ||
                    original.instructions != newInstructions
        }
    }

