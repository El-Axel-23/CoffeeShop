package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.clsMenuModelo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class clsMenuPresentador(private val vista: VistaMenu) {

    private val modelo = clsMenuModelo()

    fun cargarCategorias() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respuesta = modelo.categorias()
                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful && respuesta.body()?.success == true) {
                        vista.mostrarCategorias(respuesta.body()?.data ?: emptyList())
                    } else vista.mostrarError(respuesta.body()?.message ?: "No se pudieron cargar las categorías")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { vista.mostrarError(e.message ?: "Error de conexión") }
            }
        }
    }

    fun cargarProductos(idCategoria: Int? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respuesta = modelo.productos(idCategoria)
                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful && respuesta.body()?.success == true) {
                        vista.mostrarProductos(respuesta.body()?.data ?: emptyList())
                    } else vista.mostrarError(respuesta.body()?.message ?: "No se pudieron cargar los productos")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { vista.mostrarError(e.message ?: "Error de conexión") }
            }
        }
    }
}
interface VistaMenu {
    fun mostrarCategorias(lista: List<com.example.coffeshop.Modelo.dataCategorias>)
    fun mostrarProductos(lista: List<com.example.coffeshop.Modelo.dataProductos>)
    fun mostrarError(mensaje: String)
}