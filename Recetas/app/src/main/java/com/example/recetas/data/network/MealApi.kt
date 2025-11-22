package com.example.recetas.data.network

import com.example.recetas.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("search.php")
    suspend fun getMealByName(
        @Query("s") name: String
    ): MealResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealResponse
}
