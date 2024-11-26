package com.cursoandroid.pokedex

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cursoandroid.pokedex.Services.models.Region
import com.cursoandroid.pokedex.ui.theme.PokedexTheme
import com.cursoandroid.pokedex.ui.theme.coloresPoke

class MainActivity : ComponentActivity(){
    private val regions = listOf(
        Region("National", "https://pokeapi.co/api/v2/pokedex/1"), //ACA SE MUESTRAN TODOS LOS POKEMONES SIN FALTA
        Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2"),
        Region("Johto", "https://pokeapi.co/api/v2/pokedex/3"),
        Region("Hoenn", "https://pokeapi.co/api/v2/pokedex/4"),
        Region("Sinnoh", "https://pokeapi.co/api/v2/pokedex/5"), // Falta Pokemon 491,492 y 493
        Region("Unova", "https://pokeapi.co/api/v2/pokedex/8"),
        Region("Conquest-Gallery", "https://pokeapi.co/api/v2/pokedex/11"),
        Region("Kalos-Central", "https://pokeapi.co/api/v2/pokedex/12"),
        Region("Kalos-Coastal", "https://pokeapi.co/api/v2/pokedex/13"),
        Region("Kalos-Mountain", "https://pokeapi.co/api/v2/pokedex/14"),// Falta Pokemon 719,721 y 721
        Region("Alola", "https://pokeapi.co/api/v2/pokedex/21"), // Falta Pokemon 808 y 809
        Region("Galar", "https://pokeapi.co/api/v2/pokedex/27"),  // Falta Pokemon 891 a 898
        Region("Hisui", "https://pokeapi.co/api/v2/pokedex/30"),
        Region("Paldea", "https://pokeapi.co/api/v2/pokedex/31"),
        Region("BlueBerry", "https://pokeapi.co/api/v2/pokedex/32"),
        Region("Kitakami", "https://pokeapi.co/api/v2/pokedex/33"),)
    // Al final, faltan Pokemones del 1018 al 1025


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = coloresPoke.DarkGray.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = false

            PokedexTheme {
                val isConnected = isInternetAvailable()
                if (!isConnected) {
                    ShowNoInternetDialog(
                        onDismiss = { finish() },
                        onGoToFavorites = { goToFavorites() }
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokedexScreen(
                        regions = regions,
                        modifier = Modifier.padding(innerPadding),
                        onClickRegion = { goToRegion(it) },
                        goToFavorites = { goToFavorites() }
                    )
                }
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun goToRegion(regionId: Int) {
        val intent = Intent(this, PokemonesRegion::class.java)
        intent.putExtra("REGION_ID", regionId)
        startActivity(intent)
    }

    private fun goToFavorites() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ShowNoInternetDialog(onDismiss: () -> Unit, onGoToFavorites: () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = "Sin conexión a Internet")
            },
            text = {
                Text("No se puede acceder a la información sin conexión a Internet.")
            },
            confirmButton = {
                Button(
                    onClick = onGoToFavorites,
                    colors = ButtonDefaults.buttonColors(containerColor = coloresPoke.Blue)
                ) {
                    Text("Ir a Favoritos", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = coloresPoke.PrimaryRed)
                ) {
                    Text("Cerrar", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun PokedexScreen(
    regions: List<Region>,
    modifier: Modifier = Modifier,
    onClickRegion: (Int) -> Unit,
    goToFavorites: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .padding(WindowInsets.systemBars.asPaddingValues())
            .background(color = coloresPoke.PrimaryRed),
        topBar = { PokedexHeader(goToFavorites) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = coloresPoke.PrimaryRed)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .background(coloresPoke.DarkGray, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                items(
                    items = regions,
                    key = { it.url.split("/").last().toInt() }
                ) { region ->
                    RegionItem(region, onClickRegion)
                }
            }
        }
    }
}

@Composable
fun PokedexHeader(goToFavorites: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(coloresPoke.DarkGray)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pokédex",
            color = coloresPoke.Gold,
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            onClick = { goToFavorites() },
            colors = ButtonDefaults.buttonColors(containerColor = coloresPoke.Blue),
            modifier = Modifier.padding(start = 64.dp)
        ) {
            Text(
                text = "Favoritos",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun RegionItem(region: Region, onClickRegion: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(coloresPoke.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text(
            text = region.name.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 40.sp,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { onClickRegion(region.url.split("/").last().toInt()) },
            colors = ButtonDefaults.buttonColors(containerColor = coloresPoke.Blue)
        ) {
            Text(
                text = "Ir a región",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}