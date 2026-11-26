package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class DatosRecuperarContrasena(
    val correo: String
)