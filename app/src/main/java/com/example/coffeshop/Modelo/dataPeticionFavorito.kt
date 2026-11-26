package com.example.coffeshop.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataPeticionFavorito(
    val id_usuario: Int,
    val id_producto: Int
)