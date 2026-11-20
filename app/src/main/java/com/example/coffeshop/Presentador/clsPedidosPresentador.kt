package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.PedidoRegistrado
import com.example.coffeshop.Modelo.RegistrarPedidoRequest
import com.example.coffeshop.Modelo.clsPedidosModelo
import com.example.coffeshop.Modelo.dataPedidos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class clsPedidosPresentador(private val vista: VistaPedidos) {

    private val modelo = clsPedidosModelo()

    fun cargarPedidos(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = modelo.listar(idUsuario)
                withContext(Dispatchers.Main) {
                    if (r.isSuccessful && r.body()?.success == true)
                        vista.mostrarPedidos(r.body()?.data ?: emptyList())
                    else vista.mostrarError(r.body()?.message ?: "No se pudieron cargar los pedidos")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { vista.mostrarError(e.message ?: "Error de conexión") }
            }
        }
    }

    fun registrarPedido(pedido: RegistrarPedidoRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = modelo.registrar(pedido)
                withContext(Dispatchers.Main) {
                    if (r.isSuccessful && r.body()?.success == true && r.body()?.data != null)
                        vista.pedidoRegistrado(r.body()!!.data!!)
                    else vista.mostrarError(r.body()?.message ?: "No se pudo registrar el pedido")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { vista.mostrarError(e.message ?: "Error de conexión") }
            }
        }
    }
}
interface VistaPedidos {
    fun mostrarPedidos(lista: List<dataPedidos>)
    fun pedidoRegistrado(pedido: PedidoRegistrado)
    fun mostrarError(mensaje: String)
}