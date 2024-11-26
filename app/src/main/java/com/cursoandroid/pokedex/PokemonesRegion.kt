package com.cursoandroid.pokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cursoandroid.pokedex.Services.Api.PokemonDriver
import com.cursoandroid.pokedex.Services.models.PokemonEntrada
import com.cursoandroid.pokedex.Services.models.Region
import com.cursoandroid.pokedex.ui.theme.PokedexTheme
import com.cursoandroid.pokedex.ui.theme.coloresPoke
import java.sql.Driver

class PokemonesRegion : ComponentActivity() {
    private val driver = PokemonDriver()

    // Lista de regiones predefinidas con sus IDs
    private val regions = listOf(
        Region("National", "https://pokeapi.co/api/v2/pokedex/1"),
        Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2"),
        Region("Johto", "https://pokeapi.co/api/v2/pokedex/3"),
        Region("Hoenn", "https://pokeapi.co/api/v2/pokedex/4"),
        Region("Sinnoh", "https://pokeapi.co/api/v2/pokedex/5"),
        Region("Unova", "https://pokeapi.co/api/v2/pokedex/8"),
        Region("Conquest-Gallery", "https://pokeapi.co/api/v2/pokedex/11"),
        Region("Kalos-Central", "https://pokeapi.co/api/v2/pokedex/12"),
        Region("Kalos-Coastal", "https://pokeapi.co/api/v2/pokedex/13"),
        Region("Kalos-Mountain", "https://pokeapi.co/api/v2/pokedex/14"),
        Region("Alola", "https://pokeapi.co/api/v2/pokedex/21"),
        Region("Galar", "https://pokeapi.co/api/v2/pokedex/27"),
        Region("Hisui", "https://pokeapi.co/api/v2/pokedex/30"),
        Region("Paldea", "https://pokeapi.co/api/v2/pokedex/31"),
        Region("Blueberry", "https://pokeapi.co/api/v2/pokedex/32"),
        Region("Kitakami", "https://pokeapi.co/api/v2/pokedex/33"),

        )
    //FILTRO PARA QUE SE MUESTREN SOLAMENTE LOS POKEMONES DE ESA REGION
    private val regionRanges = mapOf(
        "Kanto" to 1..151,
        "Johto" to 152..251,
        "Hoenn" to 252..386,
        "Sinnoh" to 387..493,
        "Unova" to 494..649,
        "Conquest-Gallery" to 493..493,
        "Kalos-Central" to 650..721,
        "Kalos-Coastal" to 650..721,
        "Kalos-Mountain" to 650..721,
        "Alola" to 722..809,
        "Galar" to 810..898,
        "Hisui" to 899..905,
        "Paldea" to 906..1008,
        "Kitakami" to 1009..1010,
        "Blueberry" to 1011..1017,
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val regionId = intent.getIntExtra("REGION_ID", 0)
        val regionName = regions.find { it.url.split("/").last().toInt() == regionId }?.name ?: "Desconocida"

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = coloresPoke.DarkGray.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = true

            PokedexTheme {
                val regionPokemonList = remember { mutableStateOf<List<PokemonEntrada>>(emptyList()) }
                val filteredPokemonList = remember { mutableStateOf<List<PokemonEntrada>>(emptyList()) }
                val types = remember { mutableStateOf(listOf(
                    "Todos","normal", "fighting", "flying", "poison", "ground", "rock", "bug", "ghost", "steel", "fire", "water", "grass", "electric",
                    "psychic", "ice", "dragon", "dark", "fairy")) }
                val selectedType = remember { mutableStateOf(types.value.first()) }
                val searchQuery = remember { mutableStateOf("") }

                // Cargar datos
                driver.PokemonByRegion(
                    region = regionId.toString().lowercase(),
                    loadData = {
                        val range = regionRanges[regionName]
                        if (range != null) {
                            val filteredPokemon = it.filter { pokemon ->
                                val entryNumber = extractPokemonNumber(pokemon.especies_pokemon.url)
                                entryNumber in range
                            }
                            regionPokemonList.value = filteredPokemon
                            filteredPokemonList.value = filteredPokemon
                        } else {
                            regionPokemonList.value = it
                            filteredPokemonList.value = it
                        }
                    },
                    errorData = {
                        println("Error al cargar Pokémon de la región.")
                    }
                )
                val filterByType: (String) -> Unit = { type ->
                    selectedType.value = type
                    if (type == "Todos") {
                        filteredPokemonList.value = regionPokemonList.value
                    } else {
                        driver.PokemonsByType(
                            type = type,
                            loadData = { typePokemonList ->
                                val filtered = regionPokemonList.value.filter { regionPokemon ->
                                    val pokemonNumber = extractPokemonNumber(regionPokemon.especies_pokemon.url)
                                    typePokemonList.any { it.id_entrada == pokemonNumber }
                                }
                                filteredPokemonList.value = filtered
                            },
                            errorData = {
                                println("Error al cargar Pokémon por tipo.")
                            }
                        )
                    }
                }
                val filterBySearch: (String) -> Unit = { query ->
                    searchQuery.value = query
                    filteredPokemonList.value = regionPokemonList.value.filter { pokemon ->
                        pokemon.especies_pokemon.name.contains(query, ignoreCase = true)
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues()),
                    topBar = {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(coloresPoke.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = regionName,
                                color = coloresPoke.Gold, // Amarillo
                                style = MaterialTheme.typography.headlineLarge
                            )
                            TopBarWithBackButton(
                                title = regionName,
                                onBackClick = { finish() }
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .background(localColorBackground)
                            .fillMaxSize()
                    ) {
                        // Encabezado con el nombre de la región
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(coloresPoke.DarkGray)
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = regionName,
                                color = coloresPoke.Gold,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }

                        // Row para TypeSelector y SearchBar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Selector de tipo
                            TypeSelector(
                                types = types.value,
                                onTypeSelected = { type -> filterByType(type) },
                                modifier = Modifier.weight(1f)
                            )

                            // Barra de búsqueda
                            SearchBar(
                                query = searchQuery.value,
                                onQueryChange = { query -> filterBySearch(query) },
                                modifier = Modifier.weight(2f)
                            )
                        }

                        // Lista de Pokémon
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(
                                items = filteredPokemonList.value,
                                key = { it.id_entrada }
                            ) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    onClick = {
                                        val intent = Intent(this@PokemonesRegion, InfoPokemon::class.java)
                                        intent.putExtra("POKEMON_ID", extractPokemonNumber(pokemon.pokemon_species.url).toString())
                                        startActivity(intent)
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    // Tarjeta individual de Pokémon
    @Composable
    fun PokemonCard(pokemon: PokemonEntrada, onClick: () -> Unit) {
        CardWithPadding(
            backgroundColor = localColorPrimary,
            contentColor = Color.White,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "ID: ${extractPokemonNumber(pokemon.especies_pokemon.url)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Nombre: ${pokemon.especies_pokemon.name.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${extractPokemonNumber(pokemon.pokemon_species.url)}.png",
                    contentDescription = "Imagen del Pokémon",
                    modifier = Modifier.size(64.dp)
                )
            }
            Button(onClick = onClick, modifier = Modifier.padding(top = 8.dp)) {
                Text("Ver detalles")
            }
        }
    }

    @Composable
    fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.TextField(
            value = query,
            onValueChange = { newText ->
                onQueryChange(newText)
            },
            label = { Text("Buscar Pokémon") },
            modifier = modifier.padding(16.dp)
        )
    }

    @Composable
    fun TypeSelector(
        types: List<String>,
        onTypeSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val expanded = remember { mutableStateOf(false) }
        val selectedType = remember { mutableStateOf(types.firstOrNull() ?: "Seleccionar tipo") }

        Column(modifier = modifier) {
            Button(onClick = { expanded.value = true }) {
                Text(text = selectedType.value.replaceFirstChar { it.uppercase() })
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            expanded.value = false
                            selectedType.value = type
                            onTypeSelected(type)
                        }
                    )
                }
            }
        }
    }


    @Composable
    fun PokemonList(
        pokemonEntries: List<PokemonEntrada>,
        modifier: Modifier = Modifier,
        onClickPokemon: (String) -> Unit = {}
    ) {
        if (pokemonEntries.isEmpty()) {
            Text(text = "No hay Pokémon disponibles en esta región.")
            return
        } else {
            LazyColumn(modifier = modifier) {
                items(
                    items = pokemonEntries,
                    key = { it.id_entrada }
                ) { pokemon ->
                    Column {
                        Row {
                            val pokemonNumber = extractPokemonNumber(pokemon.especies_pokemon.url)
                            Text(text = "ID: $pokemonNumber ")

                        }
                        Row {
                            Text(text = "Nombre: ${pokemon.especies_pokemon.name.replaceFirstChar { it.uppercase() }}")
                        }
                        Row {
                            val pokemonEntryNumber = extractPokemonNumber(pokemon.especies_pokemon.url)
                            AsyncImage(
                                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonEntryNumber}.png",
                                contentDescription = "Imagen del Pokémon"
                            )
                        }

                        Button(onClick = {
                            val pokemonNumber = extractPokemonNumber(pokemon.especies_pokemon.url)
                            onClickPokemon(pokemonNumber.toString())
                        }) {

                            Text(text = "Ver detalles")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TopBarWithBackButton(
        title: String,
        onBackClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(coloresPoke.DarkGray)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón de retroceso con color personalizado
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.padding(start = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = coloresPoke.Gold, // Fondo dorado
                        contentColor = Color.Black          // Texto/ícono negro
                    )
                ) {
                    Text("Volver", style = MaterialTheme.typography.titleMedium)
                }

                // Título centrado
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = coloresPoke.Gold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }




    private fun extractPokemonNumber(url: String): Int {
        return url.trimEnd('/').split('/').last().toInt()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        PokedexTheme {
            TypeSelector(types = listOf("fire", "water", "grass"), onTypeSelected = {})
            PokemonList(
                pokemonEntries = emptyList(),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}