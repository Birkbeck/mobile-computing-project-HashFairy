package com.example.culinarycompanion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RecipeDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDAO

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recipeDao = database.recipeDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertRecipe_savesToDatabase() = runBlocking {
        val recipe = Recipe(
            name = "Chocolate Chip Cookies",
            category = "Dessert",
            ingredients = "Flour, Sugar, Butter, Chocolate Chips",
            instructions = "Mix ingredients and bake at 375°F for 12 minutes"
        )


        recipeDao.insertRecipe(recipe)


        val allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        Assert.assertEquals(1, allRecipes.size)
        Assert.assertEquals("Chocolate Chip Cookies", allRecipes[0].name)
        Assert.assertEquals("Dessert", allRecipes[0].category)
    }

    @Test
    fun getAllRecipes_returnsAllInsertedRecipes() = runBlocking {

        val recipe1 = Recipe(
            name = "Pasta",
            category = "Dinner",
            ingredients = "Pasta, Sauce",
            instructions = "Cook pasta"
        )
        val recipe2 = Recipe(
            name = "Salad",
            category = "Lunch",
            ingredients = "Lettuce, Tomato",
            instructions = "Mix vegetables"
        )
        val recipe3 = Recipe(
            name = "Toast",
            category = "Breakfast",
            ingredients = "Bread, Butter",
            instructions = "Toast bread"
        )


        recipeDao.insertRecipe(recipe1)
        recipeDao.insertRecipe(recipe2)
        recipeDao.insertRecipe(recipe3)


        val allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        Assert.assertEquals(3, allRecipes.size)

        val recipeNames = allRecipes.map { it.name }
        Assert.assertTrue(recipeNames.contains("Pasta"))
        Assert.assertTrue(recipeNames.contains("Salad"))
        Assert.assertTrue(recipeNames.contains("Toast"))
    }

    @Test
    fun getRecipeById_returnsCorrectRecipe() = runBlocking {

        val recipe = Recipe(
            name = "Chicken Curry",
            category = "Dinner",
            ingredients = "Chicken, Curry",
            instructions = "Cook chicken"
        )
        recipeDao.insertRecipe(recipe)


        val allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        val insertedRecipe = allRecipes[0]


        val retrievedRecipe = getLiveDataValue(recipeDao.getRecipeById(insertedRecipe.id))


        Assert.assertNotNull(retrievedRecipe)
        Assert.assertEquals("Chicken Curry", retrievedRecipe.name)
        Assert.assertEquals("Dinner", retrievedRecipe.category)
        Assert.assertEquals(insertedRecipe.id, retrievedRecipe.id)
    }

    @Test
    fun getRecipesByCategory_returnsFilteredRecipes() = runBlocking {
        // Arrange
        val lunchRecipe1 = Recipe(
            name = "Caesar Salad",
            category = "Lunch",
            ingredients = "Lettuce, Croutons",
            instructions = "Mix"
        )
        val lunchRecipe2 = Recipe(
            name = "Sandwich",
            category = "Lunch",
            ingredients = "Bread, Ham",
            instructions = "Assemble"
        )
        val dinnerRecipe = Recipe(
            name = "Steak",
            category = "Dinner",
            ingredients = "Beef",
            instructions = "Grill"
        )

        recipeDao.insertRecipe(lunchRecipe1)
        recipeDao.insertRecipe(lunchRecipe2)
        recipeDao.insertRecipe(dinnerRecipe)


        val lunchRecipes = getLiveDataValue(recipeDao.getRecipesByCategory("Lunch"))


        Assert.assertEquals(2, lunchRecipes.size)
        Assert.assertTrue(lunchRecipes.all { it.category == "Lunch" })

        val lunchRecipeNames = lunchRecipes.map { it.name }
        Assert.assertTrue(lunchRecipeNames.contains("Caesar Salad"))
        Assert.assertTrue(lunchRecipeNames.contains("Sandwich"))
    }

    @Test
    fun updateRecipe_updatesInDatabase() = runBlocking {
        val originalRecipe = Recipe(
            name = "Original Pizza",
            category = "Dinner",
            ingredients = "Dough, Cheese",
            instructions = "Bake"
        )
        recipeDao.insertRecipe(originalRecipe)

        val allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        val insertedRecipe = allRecipes[0]


        val updatedRecipe = insertedRecipe.copy(
            name = "Pepperoni Pizza",
            ingredients = "Dough, Cheese, Pepperoni",
            instructions = "Add pepperoni and bake"
        )
        recipeDao.updateRecipe(updatedRecipe)

        val retrievedRecipe = getLiveDataValue(recipeDao.getRecipeById(insertedRecipe.id))
        Assert.assertEquals("Pepperoni Pizza", retrievedRecipe.name)
        Assert.assertEquals("Dough, Cheese, Pepperoni", retrievedRecipe.ingredients)
        Assert.assertEquals("Add pepperoni and bake", retrievedRecipe.instructions)
        Assert.assertEquals("Dinner", retrievedRecipe.category) // Should remain unchanged
    }

    @Test
    fun deleteRecipe_removesFromDatabase() = runBlocking {
        val recipe1 = Recipe(
            name = "Recipe 1",
            category = "Lunch",
            ingredients = "Ingredients",
            instructions = "Instructions"
        )
        val recipe2 = Recipe(
            name = "Recipe 2",
            category = "Dinner",
            ingredients = "Ingredients",
            instructions = "Instructions"
        )

        recipeDao.insertRecipe(recipe1)
        recipeDao.insertRecipe(recipe2)


        var allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        Assert.assertEquals(2, allRecipes.size)


        val recipeToDelete = allRecipes.find { it.name == "Recipe 1" }!!
        recipeDao.deleteRecipe(recipeToDelete)


        allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        Assert.assertEquals(1, allRecipes.size)
        Assert.assertEquals("Recipe 2", allRecipes[0].name)
    }

    @Test
    fun emptyDatabase_returnsEmptyList() {
        val allRecipes = getLiveDataValue(recipeDao.getAllRecipes())
        Assert.assertTrue(allRecipes.isEmpty())
    }

    @Test
    fun getRecipesByCategory_nonExistentCategory_returnsEmptyList() = runBlocking {
        val recipe = Recipe(
            name = "Test Recipe",
            category = "Lunch",
            ingredients = "Test",
            instructions = "Test"
        )
        recipeDao.insertRecipe(recipe)

        val recipes = getLiveDataValue(recipeDao.getRecipesByCategory("NonExistentCategory"))

        Assert.assertTrue(recipes.isEmpty())
    }

    // Helper function to get value from LiveData for testing the database
    private fun <T> getLiveDataValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data[0] = value
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        @Suppress("UNCHECKED_CAST")
        return data[0] as T
    }
}