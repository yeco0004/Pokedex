package com.cursoandroid.pokedex.Services.Api
import com.cursoandroid.pokedex.Services.Api.ApiPokemon

import com.cursoandroid.pokedex.Services.models.PokemonEntrada
import com.cursoandroid.pokedex.Services.models.PokemonInfo
import com.cursoandroid.pokedex.Services.models.PokemonSpeciesInfo
import com.cursoandroid.pokedex.Services.models.RespuestaTipos
import com.cursoandroid.pokedex.Services.models.Sprites
import com.example.pokedex.services.models.CadenaEvolucion
import com.example.pokedex.services.models.PokemonEspecies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class RegionService: ApiPokemon (){
    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    fun getPokemonsByRegion(
        region: String,
        success: (pokemons: List<PokemonEntrada>) -> Unit,
        error: () -> Unit
    ){
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonsByRegion(region)

                val data = response.body()
                when (data) {
                    null -> success(emptyList())
                    else -> success(data.entradas_pokemon)
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }

    fun getPokemonInfo(
        nameOrId: String,
        success: (pokemon: PokemonInfo) -> Unit,
        error: () -> Unit

    ){
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonInfo(nameOrId)
                val data = response.body()
                when (data) {
                    null -> success(PokemonInfo(0, "", emptyList(), Sprites("", ""), emptyList(), emptyList(), 0, 0))
                    else -> success(data)
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }

    fun getPokemonSpecies(
        nameOrId: String,
        success: (speciesInfo: PokemonSpeciesInfo) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonSpecies(nameOrId)
                val data = response.body()
                if (data != null) {
                    success(data)
                } else {
                    success(PokemonSpeciesInfo(emptyList(), PokemonEspecies("", "")))
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }

    fun getPokemonsByType(
        type: String,
        success: (response: RespuestaTipos) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonsByType(type)
                val data = response.body()
                when (data) {
                    null -> success(RespuestaTipos(emptyList()))
                    else -> success(data)
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }

    fun getEvolutionChain(
        id: Int,
        success: (evolutionChain: CadenaEvolucion) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getEvolutionChain(id)

                val data = response.body()
                if (data != null) {
                    success(data)
                } else {
                    error()
                }
            } catch (e: Exception) {
                println("Error al obtener la cadena de evolución: $e")
                error()
            }
        }
    }

}