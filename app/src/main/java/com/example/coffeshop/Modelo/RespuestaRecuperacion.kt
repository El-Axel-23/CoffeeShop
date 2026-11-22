package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class RespuestaRecuperacion(
    val exito: Boolean,
    val mensaje: String
)
