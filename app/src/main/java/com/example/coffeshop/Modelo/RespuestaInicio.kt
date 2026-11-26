package com.example.coffeshop.Modelo

import com.google.gson.annotations.SerializedName

data class RespuestaInicio(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("categorias") val categorias: List<dataCategorias> = emptyList(),
    @SerializedName("destacados") val destacados: List<Producto> = emptyList(),
    @SerializedName("menu_completo") val menuCompleto: List<Producto> = emptyList(),
    @SerializedName("error") val error: String? = null
)