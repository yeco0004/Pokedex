package com.cursoandroid.pokedex.Services.models

import com.example.pokedex.services.models.PokemonEspecies

data class RespuestaTipos(
    val pokemon: List<PokemonWrapper>,
)

data class Tipo(
    val name: String,
    val url: String
)

data class PokemonWrapper(
    val pokemon: PokemonEspecies
)