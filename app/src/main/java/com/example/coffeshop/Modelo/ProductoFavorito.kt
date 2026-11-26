package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class ProductoFavorito(
    val id_producto: Int,
    val id_categoria: Int,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val imagen_url: String,
    val disponible: Int,
    val fecha: String
)