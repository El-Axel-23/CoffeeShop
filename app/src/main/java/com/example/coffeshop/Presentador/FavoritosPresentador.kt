
package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.FavoritosModelo
import com.example.coffeshop.Modelo.ProductoFavorito
import com.example.coffeshop.Modelo.RespuestaFavoritos

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritosPresentador(private var vista: VistaFavoritos?) {
    interface VistaFavoritos {
        fun mostrarFavoritos(productos: List<ProductoFavorito>)
        fun mostrarCargando(cargando: Boolean)
        fun mostrarError(mensaje: String)
    }
    private val modelo = FavoritosModelo()
    private var llamada: Call<RespuestaFavoritos>? = null
    // CONSULTAR FAVORITOS
    fun cargarFavoritos(idUsuario: Int) {
        if (idUsuario <= 0) {
            vista?.mostrarError("Debes iniciar sesión para ver tus favoritos.")
            return
        }
        llamada?.cancel()
        vista?.mostrarCargando(true)
        val peticion = modelo.obtenerFavoritos(idUsuario)
        llamada = peticion
        peticion.enqueue(
            object : Callback<RespuestaFavoritos> {
                override fun onResponse(call: Call<RespuestaFavoritos>, response: Response<RespuestaFavoritos>) {
                    if (call.isCanceled) {
                        return
                    }
                    llamada = null
                    vista?.mostrarCargando(false)
                    val datos = response.body()
                    if (
                        response.isSuccessful &&
                        datos != null &&
                        datos.exito
                    ) {
                        vista?.mostrarFavoritos(
                            datos.favoritos ?: emptyList()
                        )
                    } else {
                        vista?.mostrarError(datos?.mensaje ?: "No fue posible cargar los favoritos.")
                    }
                }
                override fun onFailure(call: Call<RespuestaFavoritos>, t: Throwable) {
                    if (call.isCanceled) {
                        return
                    }
                    llamada = null
                    vista?.mostrarCargando(false)
                    vista?.mostrarError("Error de conexión con el servidor.")
                }
            }
        )
    }
    fun liberar() {
        llamada?.cancel()
        llamada = null
        vista = null
    }
}