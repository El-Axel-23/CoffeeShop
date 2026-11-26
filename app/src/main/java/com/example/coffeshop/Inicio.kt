package com.example.coffeshop

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Adaptador.ProductoAdapter
import com.example.coffeshop.Modelo.Producto
import com.example.coffeshop.Modelo.RetrofitCliente
import com.example.coffeshop.Presentador.InicioPresentador

class Inicio : AppCompatActivity() {

    private lateinit var presentador: InicioPresentador
    private lateinit var adapterDestacados: ProductoAdapter
    private lateinit var adapterMenu: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val apiService = RetrofitCliente.api
        presentador = InicioPresentador(this, apiService)

        val rvDestacados = findViewById<RecyclerView>(R.id.rvDestacados)
        val rvMenuCompleto = findViewById<RecyclerView>(R.id.rvMenuCompleto)

        adapterDestacados = ProductoAdapter(
            productos = emptyList(),
            onAgregarClick = { presentador.agregarAlCarrito(it) },
            onFavoritoClick = { presentador.alternarFavorito(it) }
        )

        adapterMenu = ProductoAdapter(
            productos = emptyList(),
            onAgregarClick = { presentador.agregarAlCarrito(it) },
            onFavoritoClick = { presentador.alternarFavorito(it) }
        )

        rvDestacados.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDestacados.adapter = adapterDestacados

        rvMenuCompleto.layoutManager = GridLayoutManager(this, 2)
        rvMenuCompleto.adapter = adapterMenu

        configurarChipsCategorias()
        configurarNavegacionYAcciones()
        presentador.cargarInicio()
    }

    private fun configurarChipsCategorias() {
        val chipTodos = findViewById<TextView>(R.id.chipTodos)
        val chipCafesCalientes = findViewById<TextView>(R.id.chipCafesCalientes)
        val chipFrios = findViewById<TextView>(R.id.chipFrios)
        val chipFrappes = findViewById<TextView>(R.id.chipFrappes)

        val chips = listOf(chipTodos, chipCafesCalientes, chipFrios, chipFrappes)

        fun seleccionarChip(activo: TextView, categoria: String) {
            chips.forEach { chip ->
                if (chip == activo) {
                    chip.setBackgroundResource(R.drawable.bg_chip_inicio_activo)
                    chip.setTextColor(Color.parseColor("#FFFDF9"))
                    chip.setTypeface(chip.typeface, Typeface.BOLD)
                } else {
                    chip.setBackgroundResource(R.drawable.bg_chip_inicio_inactivo)
                    chip.setTextColor(Color.parseColor("#5E4337"))
                    chip.setTypeface(chip.typeface, Typeface.NORMAL)
                }
            }
            presentador.filtrarPorCategoria(categoria)
        }

        chipTodos.setOnClickListener { seleccionarChip(chipTodos, "Todos") }
        chipCafesCalientes.setOnClickListener { seleccionarChip(chipCafesCalientes, "Cafés calientes") }
        chipFrios.setOnClickListener { seleccionarChip(chipFrios, "Fríos") }
        chipFrappes.setOnClickListener { seleccionarChip(chipFrappes, "Frappés") }
    }

    private fun configurarNavegacionYAcciones() {
        findViewById<View>(R.id.searchBarLayout).setOnClickListener {
            mostrarMensaje("Abriendo buscador")
            // startActivity(Intent(this, BusquedaActivity::class.java))
        }

        findViewById<View>(R.id.ivNotification).setOnClickListener {
            mostrarMensaje("Abriendo notificaciones")
            // startActivity(Intent(this, NotificacionActivity::class.java))
        }

        findViewById<View>(R.id.navInicio).setOnClickListener {
            presentador.cargarInicio()
        }

        findViewById<View>(R.id.navMenu).setOnClickListener {
            mostrarMensaje("Ir a Menú")
            // startActivity(Intent(this, MenuActivity::class.java))
        }

        findViewById<View>(R.id.navCarrito).setOnClickListener {
            mostrarMensaje("Ir a Carrito")
            // startActivity(Intent(this, CarritoActivity::class.java))
        }

        findViewById<View>(R.id.navFavoritos).setOnClickListener {
            mostrarMensaje("Ir a Favoritos")
            // startActivity(Intent(this, FavoritosActivity::class.java))
        }

        findViewById<View>(R.id.navPerfil).setOnClickListener {
            mostrarMensaje("Ir a Perfil")
            // startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    fun mostrarDestacados(productos: List<Producto>) {
        adapterDestacados.actualizarLista(productos)
    }

    fun mostrarMenuCompleto(productos: List<Producto>) {
        adapterMenu.actualizarLista(productos)
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}