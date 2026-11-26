package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataRespuestaFavoritos(
    val exito: Boolean,
    val mensaje: String,
    val total: Int = 0,
    val favoritos: List<ProductoFavorito> = emptyList()
)