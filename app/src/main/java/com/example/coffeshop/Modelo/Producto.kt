package com.example.coffeshop

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String, // "Cafés calientes", "Fríos", "Frappés"
    val imagenResId: Int,  // Ej. R.mipmap.ic_launcher o tu foto en drawable
    val esDestacado: Boolean = false,
    var esFavorito: Boolean = false
)