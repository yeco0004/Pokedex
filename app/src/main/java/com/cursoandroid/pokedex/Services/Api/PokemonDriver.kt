package com.cursoandroid.pokedex.Services.Api

import com.cursoandroid.pokedex.Services.models.PokemonEntrada
import com.cursoandroid.pokedex.Services.models.PokemonInfo
import com.cursoandroid.pokedex.Services.models.PokemonSpeciesInfo
import com.example.pokedex.services.models.CadenaEvolucion
import com.example.pokedex.services.models.PokemonEspecies

class PokemonDriver {
    private val service: RegionService = RegionService()

    fun PokemonByRegion(
        region: String,
        loadData: (list: List<PokemonEntrada>) -> Unit,
        errorData: () -> Unit
    ){
        this.service.getPokemonsByRegion(
            region = region,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }

    fun PokemonInfo(
        nameOrId: String,
        loadData: (list: PokemonInfo) -> Unit,
        errorData: () -> Unit

    ){
        this.service.getPokemonInfo(
            nameOrId = nameOrId,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )

    }

    fun PokemonSpeciesInfo(
        nameOrId: String,
        loadData: (speciesInfo: PokemonSpeciesInfo) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getPokemonSpecies(
            nameOrId = nameOrId,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }


    private fun extractPokemonNumber(url: String): Int {
        return url.trimEnd('/').split('/').last().toInt()
    }

    fun PokemonsByType(
        type: String,
        loadData: (list: List<PokemonEntrada>) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getPokemonsByType(
            type = type,
            success = {
                val pokemonEntries = it.pokemon.map { pokemon ->
                    val entryNumber = extractPokemonNumber(pokemon.pokemon.url)
                    PokemonEntrada(entryNumber, PokemonEspecies(pokemon.pokemon.name, pokemon.pokemon.url))
                }
                loadData(pokemonEntries)
            },
            error = {
                errorData()
            }
        )
    }

    fun getEvolutionChain(
        id: Int,
        loadData: (evolutionChain: CadenaEvolucion) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getEvolutionChain(
            id = id,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }



}