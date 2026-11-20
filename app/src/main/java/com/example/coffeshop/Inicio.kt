package com.example.coffeshop
import com.example.coffeshop.Adaptador.productoAdaptador

import androidx.activity.enableEdgeToEdge

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Presentador.InicioContracto

class Inicio : AppCompatActivity(), InicioContracto.View {

    private lateinit var presenter: InicioContracto.Presenter
    private lateinit var adapterDestacados: productoAdaptador
    private lateinit var adapterMenu: productoAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // 1. Inicializar Presentador
        presenter = InicioPresenter(this)

        // 2. Configurar RecyclerViews
        val rvDestacados = findViewById<RecyclerView>(R.id.rvDestacados)
        val rvMenuCompleto = findViewById<RecyclerView>(R.id.rvMenuCompleto)

        adapterDestacados = ProductoAdapter(
            productos = emptyList(),
            onAgregarClick = { presenter.alAgregarAlCarrito(it) },
            onFavoritoClick = { presenter.alAlternarFavorito(it) }
        )

        adapterMenu = ProductoAdapter(
            productos = emptyList(),
            onAgregarClick = { presenter.alAgregarAlCarrito(it) },
            onFavoritoClick = { presenter.alAlternarFavorito(it) }
        )

        rvDestacados.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDestacados.adapter = adapterDestacados

        rvMenuCompleto.layoutManager = GridLayoutManager(this, 2)
        rvMenuCompleto.adapter = adapterMenu

        // 3. Cargar los datos
        presenter.cargarDatosIniciales()
    }

    override fun mostrarDestacados(productos: List<Producto>) {
        adapterDestacados.actualizarLista(productos)
    }

    override fun mostrarMenuCompleto(productos: List<Producto>) {
        adapterMenu.actualizarLista(productos)
    }

    override fun actualizarTituloDestacados(cantidad: Int) {
        // Si le agregas android:id="@+id/tvDestacadosHeader" a ese TextView en el XML, puedes actualizarlo aquí
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}