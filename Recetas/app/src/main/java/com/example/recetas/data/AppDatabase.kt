package com.example.recetas.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object AppDatabase {

    private const val FILE_NAME = "favorites.json"


    fun saveFavorite(context: Context, recipe: Recipe) {
        val list = getFavorites(context).toMutableList()


        if (!list.any { it.id == recipe.id }) {
            list.add(recipe)
        }

        saveList(context, list)
    }


    fun getFavorites(context: Context): List<Recipe> {
        return try {
            val file = context.openFileInput(FILE_NAME)
            val json = file.bufferedReader().readText()

            val array = JSONArray(json)

            List(array.length()) { i ->
                val obj = array.getJSONObject(i)

                Recipe(
                    id = obj.getInt("id"),
                    name = obj.getString("name"),
                    imageUrl = obj.getString("imageUrl")
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }


    private fun saveList(context: Context, list: List<Recipe>) {

        val array = JSONArray()

        list.forEach { recipe ->
            val obj = JSONObject()
            obj.put("id", recipe.id)
            obj.put("name", recipe.name)
            obj.put("imageUrl", recipe.imageUrl)
            array.put(obj)
        }

        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            .use { it.write(array.toString().toByteArray()) }
    }
}
