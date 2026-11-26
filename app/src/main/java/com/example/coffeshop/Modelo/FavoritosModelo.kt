package com.example.coffeshop.Modelo

import retrofit2.Call

class FavoritosModelo {

    // Consultar favoritos del usuario
    fun obtenerFavoritos(
        id_usuario: Int
    ): Call<dataRespuestaFavoritos> {

        return RetrofitCliente.api.obtenerFavoritos(
            id_usuario
        )
    }

    // Agregar un producto a favoritos
    fun agregarFavorito(
        id_usuario: Int,
        id_producto: Int
    ): Call<dataRespuestaOperacionFavorito> {

        val datos = dataPeticionFavorito(
            id_usuario,
            id_producto
        )

        return RetrofitCliente.api.agregarFavorito(
            datos
        )
    }

    // Eliminar un producto de favoritos
    fun eliminarFavorito(
        id_usuario: Int,
        id_producto: Int
    ): Call<dataRespuestaOperacionFavorito> {

        val datos = dataPeticionFavorito(
            id_usuario,
            id_producto
        )

        return RetrofitCliente.api.eliminarFavorito(
            datos
        )
    }
}