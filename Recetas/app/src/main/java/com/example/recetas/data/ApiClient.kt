package com.example.recetas.data


import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    fun fetchRandomRecipe(): Recipe? {
        val url = URL("https://www.themealdb.com/api/json/v1/1/random.php")
        val response = url.readTextFromUrl() ?: return null

        val json = JSONObject(response)
        val meals = json.getJSONArray("meals")
        val meal = meals.getJSONObject(0)

        return Recipe(
            id = meal.getString("idMeal").toInt(),
            name = meal.getString("strMeal"),
            imageUrl = meal.getString("strMealThumb")
        )


    }

    fun fetchRecipeDetailByName(name: String): RecipeDetail? {
        val url = URL("https://www.themealdb.com/api/json/v1/1/search.php?s=$name")
        val response = url.readTextFromUrl() ?: return null

        val json = JSONObject(response)
        val meals = json.getJSONArray("meals")
        val meal = meals.getJSONObject(0)

        return RecipeDetail(
            id = meal.getString("idMeal"),
            name = meal.getString("strMeal"),
            img = meal.getString("strMealThumb"),
            instructions = meal.getString("strInstructions")
        )
    }

    private fun URL.readTextFromUrl(): String? {
        return try {
            val connection = this.openConnection() as HttpURLConnection
            connection.connect()
            val input = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            input
        } catch (e: Exception) {
            null
        }
    }
}
