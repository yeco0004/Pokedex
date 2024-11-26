package com.example.pokedex.services.models


data class CadenaEvolucion(
    val chain: DetallesEvolucion
)

data class DetallesEvolucion(
    val especies: Especies,
    val evolucion_a: List<DetallesEvolucion>?
)

data class Especies(
    val name: String,
    val url: String
)