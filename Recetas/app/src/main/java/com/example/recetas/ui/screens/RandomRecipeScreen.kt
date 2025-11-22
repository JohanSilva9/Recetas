package com.example.recetas.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recetas.data.DBHelper
import org.json.JSONObject

@Composable
fun RandomRecipeScreen(
    context: Context,
    onOpenFavorites: () -> Unit,
    onOpenDetail: (Int) -> Unit
) {
    var mealId by remember { mutableStateOf(0) }
    var mealName by remember { mutableStateOf("") }
    var mealThumb by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val db = remember { DBHelper(context) }

    fun loadRandomRecipe() {
        db.fetch("https://www.themealdb.com/api/json/v1/1/random.php") { response ->
            if (response == null) {
                error = true
                return@fetch
            }

            try {
                val json = JSONObject(response)
                val meal = json.getJSONArray("meals").getJSONObject(0)

                mealId = meal.getInt("idMeal")
                mealName = meal.getString("strMeal")
                mealThumb = meal.getString("strMealThumb")
                error = false
            } catch (e: Exception) {
                error = true
            }
        }
    }

    LaunchedEffect(Unit) {
        loadRandomRecipe()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Receta aleatoria", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        if (error) {
            Text("Error al cargar", color = MaterialTheme.colorScheme.error)
        } else {
            if (mealThumb.isNotEmpty()) {
                AsyncImage(
                    model = mealThumb,
                    contentDescription = "Imagen",
                    modifier = Modifier.height(200.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(mealName, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(24.dp))

        Row {
            Button(onClick = { loadRandomRecipe() }) {
                Text("Descartar")
            }

            Spacer(Modifier.width(12.dp))

            Button(onClick = {
                if (mealId != 0 && mealName.isNotEmpty()) {
                    db.addFavorite(
                        id = mealId,
                        name = mealName,
                        imageUrl = mealThumb
                    )

                    loadRandomRecipe()
                }
            }) {
                Text("Guardar en Favoritos")
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(onClick = onOpenFavorites) {
            Text("Ver Favoritos")
        }

        Spacer(Modifier.height(20.dp))

        if (mealId != 0) {
            Button(onClick = { onOpenDetail(mealId) }) {
                Text("Ver Detalles")
            }
        }
    }
}
