package com.example.coffeshop.Interface

import com.example.coffeshop.Modelo.Cafeteria
import com.example.coffeshop.Modelo.Direccion
import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Modelo.Usuario
import com.example.coffeshop.Modelo.ApiResponse
import com.example.coffeshop.Modelo.DatosCrearCuenta
import com.example.coffeshop.Modelo.DatosFavoritos
import com.example.coffeshop.Modelo.DatosLogin
import com.example.coffeshop.Modelo.DatosRecuperarContrasena
import com.example.coffeshop.Modelo.DetallePedido
import com.example.coffeshop.Modelo.PedidoRegistrado
import com.example.coffeshop.Modelo.RegistrarPedidoRequest
import com.example.coffeshop.Modelo.RespuestaAutenticacion
import com.example.coffeshop.Modelo.RespuestaFavoritos
import com.example.coffeshop.Modelo.RespuestaRecuperacion
import com.example.coffeshop.Modelo.dataCategorias
import com.example.coffeshop.Modelo.dataOpciones
import com.example.coffeshop.Modelo.dataPedidos
import com.example.coffeshop.Modelo.dataProductos
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

import com.example.coffeshop.Modelo.Producto
import com.example.coffeshop.Modelo.RespuestaInicio

interface ApiService {

    // PERFIL, UBICACION Y AYUDA

    @GET("perfil.php")
    fun obtenerPerfil(): Call<RespuestaApi<Usuario>>

    @GET("ubicacion.php")
    fun obtenerCafeteria(): Call<RespuestaApi<List<Cafeteria>>>

    @GET("preguntas.php")
    fun obtenerPreguntas(): Call<RespuestaApi<List<PreguntaFrecuente>>>

    @GET("direcciones.php")
    fun obtenerDirecciones(): Call<RespuestaApi<List<Direccion>>>


    // CATALOGO Y PEDIDOS

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

    //LOGIN

    @POST("iniciar_sesion.php")
    fun iniciarSesion(
        @Body datos: DatosLogin
    ): Call<RespuestaAutenticacion>

    @POST("crear_cuenta.php")
    fun crearCuenta(
        @Body datos: DatosCrearCuenta
    ): Call<RespuestaAutenticacion>


    @POST("recuperar_contrasena.php")
    fun recuperarContrasena(
        @Body datos: DatosRecuperarContrasena
    ): Call<RespuestaRecuperacion>

    // API DE FAVORITOS
    @POST("favoritos.php")
    fun gestionarFavoritos(
        @Body datos: DatosFavoritos
    ): Call<RespuestaFavoritos>

    // INICIO PRODUCTOS

    @GET(value = "productos.php")
    fun obtenerInicio(
        @Query("accion") accion: String = "inicio",
        @Query("id_usuario") idUsuario: Int = 1
    ): Call<RespuestaInicio>

    @GET(value = "productos.php")
    fun obtenerProductosPorCategoria(
        @Query("accion") accion: String = "categoria",
        @Query("id_categoria") idCategoria: Int,
        @Query("id_usuario") idUsuario: Int = 1
    ): Call<RespuestaApi<List<Producto>>>

}