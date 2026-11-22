package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class DatosLogin(
    val correo: String,
    val contrasena: String
)
