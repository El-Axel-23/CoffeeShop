package com.example.coffeshop.Modelo

class clsMenuModelo {

    suspend fun categorias() = RetrofitCliente.api.obtenerCategorias()
    suspend fun productos(idCategoria: Int? = null) =
        RetrofitCliente.api.obtenerProductos(idCategoria)
    suspend fun opciones(idProducto: Int) =
        RetrofitCliente.api.obtenerOpciones(idProducto)
}