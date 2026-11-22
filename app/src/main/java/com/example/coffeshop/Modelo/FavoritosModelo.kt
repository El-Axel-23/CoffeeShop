
package com.example.coffeshop.Modelo

import retrofit2.Call

// DATOS DEL PRODUCTO FAVORITO
data class ProductoFavorito(
    val id_producto: Int = 0,
    val nombre: String = "",
    val descripcion: String? = null,
    val precio: Double = 0.0,
    val imagen_url: String? = null,
    val es_favorito: Int = 1
)
// DATOS QUE ENVIAMOS AL PHP
data class DatosFavoritos(
    val accion: String,
    val id_usuario: Int,
    val id_producto: Int? = null
)
// RESPUESTA DEL PHP
data class RespuestaFavoritos(
    val exito: Boolean = false,
    val mensaje: String? = null,
    val favoritos: List<ProductoFavorito>? = null
)
// MODELO DE FAVORITOS
class FavoritosModelo {
    // CONSULTAR FAVORITOS
    fun obtenerFavoritos(
        idUsuario: Int
    ): Call<RespuestaFavoritos> {
        val datos = DatosFavoritos(
            accion = "listar",
            id_usuario = idUsuario
        )
        return RetrofitCliente.api.gestionarFavoritos(
            datos
        )
    }
    // AGREGAR FAVORITO
    fun agregarFavorito(
        idUsuario: Int,
        idProducto: Int
    ): Call<RespuestaFavoritos> {
        val datos = DatosFavoritos(
            accion = "agregar",
            id_usuario = idUsuario,
            id_producto = idProducto
        )
        return RetrofitCliente.api.gestionarFavoritos(
            datos
        )
    }
    // QUITAR FAVORITO
    fun quitarFavorito(
        idUsuario: Int,
        idProducto: Int
    ): Call<RespuestaFavoritos> {
        val datos = DatosFavoritos(
            accion = "quitar",
            id_usuario = idUsuario,
            id_producto = idProducto
        )
        return RetrofitCliente.api.gestionarFavoritos(
            datos
        )
    }
}