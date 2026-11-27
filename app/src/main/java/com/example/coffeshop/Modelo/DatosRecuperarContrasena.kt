package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class DatosRecuperarContrasena(
    val accion: String,
    val correo: String? = null,
    val token: String? = null,
    val nueva_contrasena: String? = null,
    val confirmar_contrasena: String? = null
)
