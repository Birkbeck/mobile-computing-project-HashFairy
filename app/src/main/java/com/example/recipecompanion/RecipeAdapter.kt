package com.example.recipecompanion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter for displaying recipe cards
 * Placeholder class for Coursework 1 - functionality to be implemented in Coursework 2
 */
class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        // TODO: Implement data binding in Coursework 2
        // val recipe = recipes[position]
        // holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        // TODO: Return actual recipe count in Coursework 2
        return recipes.size
    }

    /**
     * ViewHolder class for recipe cards
     */
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
        private val recipeTitle: TextView = itemView.findViewById(R.id.recipe_title)
        private val recipeCategory: TextView = itemView.findViewById(R.id.recipe_category)
        private val btnRecipeMenu: ImageButton = itemView.findViewById(R.id.btn_recipe_menu)

        init {
            // TODO: Set up click listeners in Coursework 2
        }

        /**
         * Bind recipe data to views
         * TODO: Implement in Coursework 2
         */
        fun bind(recipe: Recipe) {
            // TODO: Set recipe title, category, image in Coursework 2
            // TODO: Set up menu button click listener in Coursework 2
        }
    }
}