package com.example.recetas.ui.screens

import androidx.compose.foundation.background
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recetas.data.DBHelper
import com.example.recetas.data.Recipe

@Composable
fun FavoritesScreen(
    context: Context,
    onSelect: (String) -> Unit,
    onReturn: () -> Unit
) {

    val db = DBHelper(context)

    var favs by remember { mutableStateOf(listOf<Recipe>()) }

    // Cargar favoritos al abrir la pantalla
    LaunchedEffect(true) {
        favs = db.getFavorites()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF36393C))
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

        Text(
            "Mis Favoritos",
            color = Color.White,
            fontSize = 26.sp
        )

        Spacer(Modifier.height(20.dp))

        favs.forEach { rec ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEAA928))
                    .padding(12.dp)
            ) {

                // IMAGEN
                AsyncImage(
                    model = rec.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelect(rec.id.toString()) }
                )

                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                        .clickable { onSelect(rec.id.toString()) }
                ) {
                    Text(rec.name, fontSize = 20.sp, color = Color(0xFF36393C))
                }


                Button(
                    onClick = {
                        db.deleteFavorite(rec.id)
                        favs = db.getFavorites()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("X", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
