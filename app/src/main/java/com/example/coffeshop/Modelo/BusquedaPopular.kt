package com.example.coffeshop.Modelo

import com.google.gson.annotations.SerializedName

data class BusquedaPopular(
    @SerializedName("termino") val termino: String = "",
    @SerializedName("veces") val veces: Int? = null
)