package com.example.recetas.data.model

data class MealResponse(
    val meals: List<Meal>
)

data class Meal(
    val idMeal: String?,
    val strMeal: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strTags: String?
)
