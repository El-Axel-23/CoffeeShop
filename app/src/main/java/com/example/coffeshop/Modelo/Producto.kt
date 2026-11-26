package com.example.coffeshop.Modelo

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id_producto") val id: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("descripcion") val descripcion: String = "",
    @SerializedName("precio") val precio: Double = 0.0,
    @SerializedName("categoria") val categoria: String = "",
    val imagenResId: Int = android.R.drawable.ic_menu_gallery,
    @SerializedName("es_destacado") val esDestacado: Boolean = false,
    @SerializedName("es_favorito") var esFavorito: Boolean = false
)