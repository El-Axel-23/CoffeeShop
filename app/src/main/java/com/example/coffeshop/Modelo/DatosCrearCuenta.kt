package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class DatosCrearCuenta(
    val nombre_completo: String,
    val correo: String,
    val contrasena: String,
    val confirmar_contrasena: String,
    val acepto_terminos: Boolean
)
