package com.example.pokedex.services.models

import com.cursoandroid.pokedex.Services.models.PokemonEntrada

data class RespuestaPokedex(
    val entradas_pokemon: List<PokemonEntrada>
)

data class PokemonEspecies(
    val name: String,
    val url: String
)