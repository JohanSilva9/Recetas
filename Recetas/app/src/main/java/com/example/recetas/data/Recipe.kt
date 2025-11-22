package com.example.recetas.data

data class Recipe(
    val id: Int,
    val name: String,
    val imageUrl: String
)



data class RecipeDetail(
    val id: String,
    val name: String,
    val img: String,
    val instructions: String
)

