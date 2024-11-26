package com.cursoandroid.pokedex.Services.models




data class PokemonInfo(
    val id: Int,                     // ID único del pokémon
    val name: String,                // Nombre del pokémon
    val types: List<PokemonType>,    // Lista de tipos del pokémon
    val sprites: Sprites,            // Imágenes del pokémon
    val abilities: List<Ability>,    // Habilidades del pokémon
    val stats: List<Stat>,           // Estadísticas del pokémon
    val weight: Int,                 // Peso
    val height: Int                  // Altura
)

data class PokemonType(
    val slot: Int,    // Orden del tipo
    val type: Tipo
)



data class Sprites(
    val front_default: String?,  // Imagen frontal estándar
    val front_shiny: String?     // Imagen frontal shiny
)

data class Ability(
    val ability: NamedResource, // Información de la habilidad
    val is_hidden: Boolean      // Si la habilidad es oculta
)

data class Stat(
    val base_stat: Int,         // Valor base de la estadística
    val effort: Int,            // Esfuerzo otorgado al derrotarlo
    val stat: NamedResource     // Información de la estadística
)

data class NamedResource(
    val name: String, // Nombre del recurso
    val url: String   // URL del recurso
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

data class Language(
    val name: String
)

data class PokemonSpeciesInfo(
    val flavor_text_entries: List<FlavorTextEntry>,
    val evolution_chain: PokemonSpeciesInfo
)