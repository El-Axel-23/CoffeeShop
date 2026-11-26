package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.FavoritosModelo
import com.example.coffeshop.Modelo.ProductoFavorito
import com.example.coffeshop.Modelo.dataRespuestaFavoritos
import com.example.coffeshop.Modelo.dataRespuestaOperacionFavorito

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritosPresentador(vistaFavoritos: VistaFavoritos) {
    private var vista: VistaFavoritos? = vistaFavoritos
    private val modelo = FavoritosModelo()
    private var peticionFavoritos: Call<dataRespuestaFavoritos>? = null
    private var peticionOperacion: Call<dataRespuestaOperacionFavorito>? = null
    interface VistaFavoritos {
        fun mostrarFavoritos(productos: List<ProductoFavorito>)
        fun mostrarCargando(cargando: Boolean)
        fun mostrarError(mensaje: String)
        fun mostrarOperacion(id_producto: Int, mensaje: String, es_favorito: Boolean)
    }
    // Obtener favoritos por id_usuario
    fun cargarFavoritos(id_usuario: Int) {
        if (id_usuario <= 0) {
            vista?.mostrarError("Usuario no válido.")
            return
        }
        peticionFavoritos?.cancel()
        vista?.mostrarCargando(true)
        val peticion = modelo.obtenerFavoritos(id_usuario)
        peticionFavoritos = peticion
        peticion.enqueue(object : Callback<dataRespuestaFavoritos> {
                override fun onResponse(call: Call<dataRespuestaFavoritos>, response: Response<dataRespuestaFavoritos>) {
                    if (call !== peticionFavoritos || call.isCanceled) {
                        return
                    }
                    vista?.mostrarCargando(false)
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        if (respuesta != null) {
                            if (respuesta.exito) {
                                vista?.mostrarFavoritos(respuesta.favoritos)
                            } else {
                                vista?.mostrarError(respuesta.mensaje)
                            }
                        } else {
                            vista?.mostrarError("Respuesta vacía.")
                        }
                    } else {
                        vista?.mostrarError("Error del servidor: " + response.code())
                    }
                }
                override fun onFailure(call: Call<dataRespuestaFavoritos>, t: Throwable) {
                    if (call !== peticionFavoritos || call.isCanceled) {
                        return
                    }
                    vista?.mostrarCargando(false)
                    vista?.mostrarError("Error de conexión: " + (t.message ?: "Sin conexión"))
                }
            }
        )
    }
    // Agregar favorito
    fun agregarFavorito(id_usuario: Int, id_producto: Int) {
        if (id_usuario <= 0 || id_producto <= 0) {
            vista?.mostrarError("Los datos no son válidos.")
            return
        }
        if (peticionOperacion != null) {
            return
        }
        val peticion = modelo.agregarFavorito(id_usuario, id_producto)
        ejecutarOperacion(peticion, id_usuario, id_producto)
    }
    // Eliminar favorito
    fun eliminarFavorito(id_usuario: Int, id_producto: Int) {
        if (id_usuario <= 0 || id_producto <= 0) {
            vista?.mostrarError("Los datos no son válidos.")
            return
        }
        if (peticionOperacion != null) {
            return
        }
        val peticion = modelo.eliminarFavorito(id_usuario, id_producto)
        ejecutarOperacion(peticion, id_usuario, id_producto)
    }
    // Procesar la respuesta de agregar o eliminar
    private fun ejecutarOperacion(peticion: Call<dataRespuestaOperacionFavorito>, id_usuario: Int, id_producto: Int) {
        peticionOperacion = peticion
        peticion.enqueue(
            object : Callback<dataRespuestaOperacionFavorito> {
                override fun onResponse(call: Call<dataRespuestaOperacionFavorito>, response: Response<dataRespuestaOperacionFavorito>) {
                    if (call !== peticionOperacion || call.isCanceled) {
                        return
                    }
                    peticionOperacion = null
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        if (respuesta != null) {
                            if (respuesta.exito) {
                                vista?.mostrarOperacion(id_producto, respuesta.mensaje, respuesta.es_favorito)
                                cargarFavoritos(id_usuario)
                            } else {
                                vista?.mostrarError(respuesta.mensaje)
                            }
                        } else {
                            vista?.mostrarError("Respuesta vacía.")
                        }
                    } else {
                        vista?.mostrarError("Error del servidor: " + response.code())
                    }
                }
                override fun onFailure(call: Call<dataRespuestaOperacionFavorito>, t: Throwable) {
                    if (call !== peticionOperacion || call.isCanceled) {
                        return
                    }
                    peticionOperacion = null
                    vista?.mostrarError("Error de conexión: " + (t.message ?: "Sin conexión"))
                }
            }
        )
    }
    // Cancelar peticiones al cerrar la vista
    fun liberar() {
        peticionFavoritos?.cancel()
        peticionOperacion?.cancel()
        peticionFavoritos = null
        peticionOperacion = null
        vista = null
    }
}