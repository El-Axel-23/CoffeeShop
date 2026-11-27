package com.example.coffeshop.Vista

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeshop.Adaptador.PedidosAdaptador
import com.example.coffeshop.Modelo.PedidoRegistrado
import com.example.coffeshop.Modelo.dataPedidos
import com.example.coffeshop.Presentador.VistaPedidos
import com.example.coffeshop.Presentador.clsPedidosPresentador
import com.example.coffeshop.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity(), VistaPedidos {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var presentador: clsPedidosPresentador
    private val lista = mutableListOf<dataPedidos>()
    private lateinit var adaptador: PedidosAdaptador

    // Sustituye por el id_usuario que obtengas al iniciar sesión.
    private val idUsuario = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presentador = clsPedidosPresentador(this)
        adaptador = PedidosAdaptador(lista)
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adaptador
        presentador.cargarPedidos(idUsuario)
    }

    override fun mostrarPedidos(pedidos: List<dataPedidos>) {
        lista.clear()
        lista.addAll(pedidos)
        adaptador.notifyDataSetChanged()
        binding.txtVacio.text = if (lista.isEmpty()) "Todavía no tienes pedidos." else ""
    }

    override fun pedidoRegistrado(pedido: PedidoRegistrado) {
        Toast.makeText(this, "Pedido #${pedido.id_pedido} registrado", Toast.LENGTH_LONG).show()
        presentador.cargarPedidos(idUsuario)
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}