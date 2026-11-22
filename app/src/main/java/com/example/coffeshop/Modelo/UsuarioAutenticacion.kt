package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioAutenticacion(
    val id_usuario: Int,
    val nombre_completo: String,
    val correo: String,
    val rol: String
)
