package com.example.recetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.recetas.ui.screens.DetailScreen
import com.example.recetas.ui.screens.FavoritesScreen
import com.example.recetas.ui.screens.RandomRecipeScreen
import com.example.recetas.ui.theme.RecetasTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            RecetasTheme {

                val ctx = LocalContext.current
                var currentScreen by remember { mutableStateOf("random") }
                var selectedRecipe by remember { mutableStateOf("") }
                when (currentScreen) {


                    "random" -> RandomRecipeScreen(
                        context = ctx,
                        onOpenFavorites = {
                            currentScreen = "favorites"
                        },
                        onOpenDetail = { recipeId ->
                            selectedRecipe = recipeId.toString()
                            currentScreen = "detail"
                        }
                    )


                    "favorites" -> FavoritesScreen(
                        context = ctx,
                        onSelect = { recipeId ->
                            selectedRecipe = recipeId
                            currentScreen = "detail"
                        },
                        onReturn = {
                            currentScreen = "random"
                        }
                    )

                    "detail" -> DetailScreen(
                        context = ctx,
                        recipeName = selectedRecipe,
                        onReturn = {
                            currentScreen = "random"
                        }
                    )
                }
            }
        }
    }
}
