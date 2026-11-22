package com.example.coffeshop.Vista

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeshop.Adaptador.ProductosAdaptador
import com.example.coffeshop.Modelo.dataCategorias
import com.example.coffeshop.Modelo.dataProductos
import com.example.coffeshop.Presentador.VistaMenu
import com.example.coffeshop.Presentador.clsMenuPresentador
import com.example.coffeshop.databinding.ActivityMenuBinding
import kotlin.collections.map

class MenuActivity : AppCompatActivity(), VistaMenu {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var presentador: clsMenuPresentador
    private val categorias = mutableListOf<dataCategorias>()
    private val productos = mutableListOf<dataProductos>()
    private lateinit var adaptador: ProductosAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presentador = clsMenuPresentador(this)
        adaptador = ProductosAdaptador(productos) { producto ->
            Toast.makeText(this, producto.nombre, Toast.LENGTH_SHORT).show()
            // Aquí puedes abrir tu pantalla de detalle del producto.
        }
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adaptador

        binding.btnTodos.setOnClickListener { presentador.cargarProductos() }
        presentador.cargarCategorias()
        presentador.cargarProductos()
    }

    override fun mostrarCategorias(lista: List<dataCategorias>) {
        categorias.clear()
        categorias.addAll(lista)
        val nombres = listOf("Todos") + lista.map { it.nombre }
        binding.spCategorias.adapter = ArrayAdapter(
            this, R.layout.simple_spinner_dropdown_item, nombres
        )
        binding.spCategorias.setSelection(0)
        binding.spCategorias.setOnItemSelectedListener(object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?, view: android.view.View?,
                position: Int, id: Long
            ) {
                val idCategoria = if (position == 0) null else categorias[position - 1].id_categoria
                presentador.cargarProductos(idCategoria)
            }
        })
    }

    override fun mostrarProductos(lista: List<dataProductos>) {
        productos.clear()
        productos.addAll(lista)
        adaptador.notifyDataSetChanged()
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}