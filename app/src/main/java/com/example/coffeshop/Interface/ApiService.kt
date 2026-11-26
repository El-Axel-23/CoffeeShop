package com.example.coffeshop.Interface

import com.example.coffeshop.Modelo.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @GET("obtener_categorias.php")
    suspend fun obtenerCategorias(): Response<ApiResponse<List<dataCategorias>>>

    @GET("obtener_productos.php")
    suspend fun obtenerProductos(
        @Query("id_categoria") idCategoria: Int? = null
    ): Response<ApiResponse<List<dataProductos>>>

    @GET("obtener_opciones.php")
    suspend fun obtenerOpciones(
        @Query("id_producto") idProducto: Int
    ): Response<ApiResponse<List<dataOpciones>>>

    @POST("registrar_pedido.php")
    suspend fun registrarPedido(
        @Body pedido: RegistrarPedidoRequest
    ): Response<ApiResponse<PedidoRegistrado>>

    @GET("obtener_pedidos.php")
    suspend fun obtenerPedidos(
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<List<dataPedidos>>>

    @GET("detalle_pedido.php")
    suspend fun detallePedido(
        @Query("id_pedido") idPedido: Int,
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<DetallePedido>>

}