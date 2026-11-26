package com.example.coffeshop.Presentador

import com.example.coffeshop.Inicio
import com.example.coffeshop.Interface.ApiService
import com.example.coffeshop.Modelo.Producto
import com.example.coffeshop.Modelo.RespuestaInicio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InicioPresentador(
    private val vista: Inicio,
    private val apiService: ApiService
) {
    private var listaDestacadosCompleta: List<Producto> = emptyList()
    private var listaMenuCompleta: List<Producto> = emptyList()

    fun cargarInicio() {
        apiService.obtenerInicio().enqueue(object : Callback<RespuestaInicio> {
            override fun onResponse(
                call: Call<RespuestaInicio>,
                response: Response<RespuestaInicio>
            ) {
                val cuerpo = response.body()
                if (response.isSuccessful && cuerpo != null && cuerpo.success) {
                    listaDestacadosCompleta = cuerpo.destacados
                    listaMenuCompleta = cuerpo.menuCompleto

                    vista.mostrarDestacados(listaDestacadosCompleta)
                    vista.mostrarMenuCompleto(listaMenuCompleta)
                } else {
                    vista.mostrarMensaje(cuerpo?.error ?: "No se pudieron cargar los productos")
                }
            }

            override fun onFailure(call: Call<RespuestaInicio>, t: Throwable) {
                vista.mostrarMensaje("Error de red: ${t.localizedMessage ?: "intenta de nuevo"}")
            }
        })
    }

    fun filtrarPorCategoria(categoriaSeleccionada: String) {
        if (categoriaSeleccionada.equals("Todos", ignoreCase = true)) {
            vista.mostrarMenuCompleto(listaMenuCompleta)
            return
        }

        val filtrados = listaMenuCompleta.filter { producto ->
            producto.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        }
        vista.mostrarMenuCompleto(filtrados)
    }

    fun agregarAlCarrito(producto: Producto) {
        vista.mostrarMensaje("Agregado al carrito: ${producto.nombre}")
    }

    fun alternarFavorito(producto: Producto) {
        producto.esFavorito = !producto.esFavorito
        val estado = if (producto.esFavorito) "en favoritos" else "fuera de favoritos"
        vista.mostrarMensaje("${producto.nombre} ahora está $estado")
    }
}