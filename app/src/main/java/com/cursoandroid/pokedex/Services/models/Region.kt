package com.cursoandroid.pokedex.Services.models

import com.google.gson.annotations.SerializedName

data class RespuestaRegion(
    @SerializedName("results")
    val results: List<Region>
)

data class Region(
    val name: String,
    val url: String
)