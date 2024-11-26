package com.cursoandroid.pokedex.Services.Api

import com.cursoandroid.pokedex.Services.models.PokemonInfo
import com.cursoandroid.pokedex.Services.models.PokemonSpeciesInfo
import com.cursoandroid.pokedex.Services.models.RespuestaRegion
import com.cursoandroid.pokedex.Services.models.RespuestaTipos
import com.example.pokedex.services.models.CadenaEvolucion
import com.example.pokedex.services.models.RespuestaPokedex
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService{

    //obtener regiones
    @GET("region")
    suspend fun getAllRegions(): Response<RespuestaRegion>

    //obtener pokemon por region
    @GET("pokedex/{region}")
    suspend fun getPokemonsByRegion(@Path("region") region: String) : Response<RespuestaPokedex>

    //obtener info de un pokemon
    @GET("pokemon/{nameOrId}/")
    suspend fun getPokemonInfo(@Path("nameOrID") nameOrId: String): Response<PokemonInfo>

    //obtener cadena de evoluciones
    @GET("evolution-chain/{id}/")
    suspend fun getEvolutionChain(@Path("id") id: Int): Response<CadenaEvolucion>

    //obtener lista de tipos de pokemon
    @GET("type/{type}")
    suspend fun getPokemonsByType(@Path("type") type: String): Response<RespuestaTipos>

    //ApiService
    @GET("pokemon-species/{nameOrId}")
    suspend fun getPokemonSpecies(@Path("nameOrId") nameOrId: String): Response<PokemonSpeciesInfo>
}