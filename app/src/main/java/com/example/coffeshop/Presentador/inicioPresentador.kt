package com.example.coffeshop.Presentador

import com.example.coffeshop.Producto
import com.example.coffeshop.R

class InicioPresentador(private val view: InicioContracto.View) : InicioContracto.Presenter {

    private val listaCompleta = listOf(
        Producto(
            id = 1,
            nombre = "Latte Vainilla Avena",
            descripcion = "Nuestro café de autor con leche de avena cremosa y toque de vainilla.",
            precio = 65.0,
            categoria = "Cafés calientes",
            imagenResId = R.mipmap.ic_launcher,
            esDestacado = true
        ),
        Producto(
            id = 2,
            nombre = "Mocha Chocolate",
            descripcion = "Intenso espresso con chocolate amargo, leche vaporizada y cacao.",
            precio = 72.0,
            categoria = "Cafés calientes",
            imagenResId = R.mipmap.ic_launcher,
            esDestacado = true
        ),
        Producto(
            id = 3,
            nombre = "Cold Brew Caramelo",
            descripcion = "Extracción en frío por 16 horas con jarabe artesanal de caramelo.",
            precio = 68.0,
            categoria = "Fríos",
            imagenResId = R.mipmap.ic_launcher,
            esDestacado = false
        ),
        Producto(
            id = 4,
            nombre = "Frappé Moka Oreo",
            descripcion = "Mezcla helada de café espresso, crema batida y trozos de galleta.",
            precio = 78.0,
            categoria = "Frappés",
            imagenResId = R.mipmap.ic_launcher,
            esDestacado = false
        )
    )

    override fun cargarDatosIniciales() {
        val destacados = listaCompleta.filter { it.esDestacado }
        view.actualizarTituloDestacados(destacados.size)
        view.mostrarDestacados(destacados)
        view.mostrarMenuCompleto(listaCompleta)
    }

    override fun filtrarPorCategoria(categoria: String) {
        val filtrados = if (categoria == "Todos") {
            listaCompleta
        } else {
            listaCompleta.filter { it.categoria.equals(categoria, ignoreCase = true) }
        }
        view.mostrarMenuCompleto(filtrados)
    }

    override fun buscarProducto(texto: String) {
        val query = texto.trim()
        val resultados = if (query.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.descripcion.contains(query, ignoreCase = true)
            }
        }
        view.mostrarMenuCompleto(resultados)
    }

    override fun alAgregarAlCarrito(producto: Producto) {
        view.mostrarMensaje("${producto.nombre} agregado al carrito")
    }

    override fun alAlternarFavorito(producto: Producto) {
        producto.esFavorito = !producto.esFavorito
        val estado = if (producto.esFavorito) "agregado a" else "eliminado de"
        view.mostrarMensaje("${producto.nombre} $estado favoritos")
    }
}