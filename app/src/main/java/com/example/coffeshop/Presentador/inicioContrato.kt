package com.example.coffeshop.Presentador
import com.example.coffeshop.Producto

interface InicioContracto {
    interface View {
        fun mostrarDestacados(productos: List<Producto>)
        fun mostrarMenuCompleto(productos: List<Producto>)
        fun actualizarTituloDestacados(cantidad: Int)
        fun mostrarMensaje(mensaje: String)
    }

    interface Presenter {
        fun cargarDatosIniciales()
        fun filtrarPorCategoria(categoria: String)
        fun buscarProducto(texto: String)
        fun alAgregarAlCarrito(producto: Producto)
        fun alAlternarFavorito(producto: Producto)
    }
}