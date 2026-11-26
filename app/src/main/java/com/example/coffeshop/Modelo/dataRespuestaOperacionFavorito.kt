package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataRespuestaOperacionFavorito(
    val exito: Boolean,
    val mensaje: String,
    val es_favorito: Boolean = false
)