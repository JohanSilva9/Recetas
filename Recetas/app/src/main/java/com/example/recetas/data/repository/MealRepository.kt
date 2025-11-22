package com.example.recetas.data.repository

import com.example.recetas.data.network.RetrofitInstance

class MealRepository {

    suspend fun getRandomMeal() = RetrofitInstance.api.getRandomMeal()

    suspend fun getMealByName(name: String) = RetrofitInstance.api.getMealByName(name)

    suspend fun getMealById(id: String) = RetrofitInstance.api.getMealById(id)
}
