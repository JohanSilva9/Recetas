package com.example.recetas.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recetas.data.DBHelper
import org.json.JSONObject

@Composable
fun DetailScreen(
    context: Context,
    recipeName: String,
    onReturn: () -> Unit
) {
    var thumb by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(recipeName) {

        val db = DBHelper(context)
        val url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$recipeName"

        db.fetch(url) { response ->

            if (response == null) {
                error = true
                return@fetch
            }

            try {
                val json = JSONObject(response)
                val meal = json.getJSONArray("meals").getJSONObject(0)

                name = meal.getString("strMeal")
                thumb = meal.getString("strMealThumb")
                instructions = meal.getString("strInstructions")

                error = false

            } catch (e: Exception) {
                error = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF36393c))
            .padding(16.dp)
    ) {

        Button(
            onClick = onReturn,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE37E1E)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Volver", color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color(0xFFEAA928))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (thumb.isNotEmpty()) {
                    AsyncImage(
                        model = thumb,
                        contentDescription = "Imagen receta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (error) {
            Text("Error al cargar detalles", color = Color.Red)
            return@Column
        }

        Text(
            "Instrucciones",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFF0C82F)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            instructions,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
