package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class RespuestaAutenticacion(
    val exito: Boolean,
    val mensaje: String,
    val usuario: UsuarioAutenticacion? = null
)
