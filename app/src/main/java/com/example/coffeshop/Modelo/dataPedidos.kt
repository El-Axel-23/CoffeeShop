package com.example.coffeshop.Modelo

data class dataPedidos(
    val id_pedido: Int,
    val tipo_entrega: String,
    val metodo_pago: String,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double,
    val estado: String,
    val fecha: String
)
data class ItemPedidoRequest(
    val id_producto: Int,
    val cantidad: Int,
    val opciones: List<Int> = emptyList()
)

data class RegistrarPedidoRequest(
    val id_usuario: Int,
    val tipo_entrega: String,
    val id_direccion: Int? = null,
    val items: List<ItemPedidoRequest>
)

data class PedidoRegistrado(
    val id_pedido: Int,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double
)

data class OpcionPedidoDetalle(
    val nombre: String,
    val precio_extra: Double
)

data class ProductoPedidoDetalle(
    val id_detalle: Int,
    val nombre_producto: String,
    val precio_unitario: Double,
    val cantidad: Int,
    val subtotal: Double,
    val opciones: List<OpcionPedidoDetalle>
)

data class DetallePedido(
    val id_pedido: Int,
    val tipo_entrega: String,
    val metodo_pago: String,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double,
    val estado: String,
    val fecha: String,
    val productos: List<ProductoPedidoDetalle>
)
