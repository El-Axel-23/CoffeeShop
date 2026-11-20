package com.example.coffeshop.Modelo

class clsPedidosModelo {

    suspend fun registrar(pedido: RegistrarPedidoRequest) =
        RetrofitCliente.api.registrarPedido(pedido)

    suspend fun listar(idUsuario: Int) =
        RetrofitCliente.api.obtenerPedidos(idUsuario)

    suspend fun detalle(idPedido: Int, idUsuario: Int) =
        RetrofitCliente.api.detallePedido(idPedido, idUsuario)
}