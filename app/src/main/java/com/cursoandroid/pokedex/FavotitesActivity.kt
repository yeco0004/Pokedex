package com.example.pokedex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cursoandroid.pokedex.CardWithPadding
import com.cursoandroid.pokedex.MainActivity
import com.cursoandroid.pokedex.Services.models.PokemonInfo
import com.cursoandroid.pokedex.ui.theme.PokedexTheme
import com.cursoandroid.pokedex.ui.theme.coloresPoke
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.ui.theme.PokedexColors
import com.example.pokedex.ui.theme.PokedexTheme
import com.google.gson.Gson
import java.util.Locale

class FavoritesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteIds = sharedPreferences.all.keys.toList()

        setContent {
            PokedexTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Favoritos", color = Color.White)
                            },
                            modifier = Modifier.background(coloresPoke.DarkGray),
                        )
                    },
                    content = { paddingValues ->
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                        ) {
                            // Fila con los botones
                            Row(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Button(
                                    onClick = { goToMainActivity() },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Volver al menú principal")
                                }

                                Button(
                                    onClick = { onBackPressed() },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("Volver")
                                }
                            }

                            // Lista de favoritos
                            LazyColumn {
                                items(favoriteIds) { id ->
                                    val pokemonJson = sharedPreferences.getString(id, null)
                                    if (pokemonJson != null) {
                                        val pokemonInfo = Gson().fromJson(pokemonJson, PokemonInfo::class.java)
                                        PokemonItem(pokemonInfo, onRemoveFavorite = {
                                            removeFavorite(sharedPreferences, id)
                                        })
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun removeFavorite(sharedPreferences: SharedPreferences, pokemonId: String) {
        val editor = sharedPreferences.edit()
        editor.remove(pokemonId)
        editor.apply()
        recreate()
    }


    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PokemonItem(pokemonInfo: PokemonInfo, onRemoveFavorite: () -> Unit) {
    CardWithPadding(
        backgroundColor = coloresPoke.DarkGray,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "ID: ${pokemonInfo.id}")
            Text(text = "Nombre: ${pokemonInfo.name.capitalize(Locale.ROOT)}")

            if (pokemonInfo.sprites.front_default != null) {
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonInfo.id}.png",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "No se logró cargar la imagen")
            }

            Text(
                text = "Peso: ${String.format("%.1f", pokemonInfo.weight / 10.0)}"
            )

            Text(text = "Altura: ${pokemonInfo.height}")

            Text(text = "Tipos: ${pokemonInfo.types.joinToString { it.type.name }}")
            Text(text = "Habilidades: ${pokemonInfo.abilities.joinToString { it.ability.name }}")

            Column {
                Text(text = "Estadísticas:")
                pokemonInfo.stats.forEach { stat ->
                    Text(text = "${stat.stat.name.capitalize()}: ${stat.base_stat}")
                }
            }

            Button(
                onClick = { onRemoveFavorite() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Eliminar de favoritos")
            }
        }
    }
}
