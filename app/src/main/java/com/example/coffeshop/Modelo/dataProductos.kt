package com.example.coffeshop.Modelo

data class dataProductos(

    val id_producto: Int,
    val id_categoria: Int,
    val categoria: String?,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val imagen_url: String?,
    val destacado: Int,
    val disponible: Int
)
