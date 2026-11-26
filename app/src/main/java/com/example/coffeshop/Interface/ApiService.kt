package com.example.coffeshop.Interface

import com.example.coffeshop.Modelo.Cafeteria
import com.example.coffeshop.Modelo.Direccion
import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Modelo.Usuario
import com.example.coffeshop.Modelo.ApiResponse
import com.example.coffeshop.Modelo.DatosCrearCuenta

import com.example.coffeshop.Modelo.DatosLogin
import com.example.coffeshop.Modelo.DatosRecuperarContrasena
import com.example.coffeshop.Modelo.DetallePedido
import com.example.coffeshop.Modelo.PedidoRegistrado
import com.example.coffeshop.Modelo.RegistrarPedidoRequest
import com.example.coffeshop.Modelo.RespuestaAutenticacion

import com.example.coffeshop.Modelo.RespuestaRecuperacion
import com.example.coffeshop.Modelo.dataCategorias
import com.example.coffeshop.Modelo.dataOpciones
import com.example.coffeshop.Modelo.dataPedidos
import com.example.coffeshop.Modelo.dataPeticionFavorito
import com.example.coffeshop.Modelo.dataProductos
import com.example.coffeshop.Modelo.dataRespuestaFavoritos
import com.example.coffeshop.Modelo.dataRespuestaOperacionFavorito
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    // LOGIN

    @POST("Login/iniciar_sesion.php")
    fun iniciarSesion(
        @Body datos: DatosLogin
    ): Call<RespuestaAutenticacion>

    @POST("Login/crear_cuenta.php")
    fun crearCuenta(
        @Body datos: DatosCrearCuenta
    ): Call<RespuestaAutenticacion>

    @POST("Login/recuperar_contrasena.php")
    fun recuperarContrasena(
        @Body datos: DatosRecuperarContrasena
    ): Call<RespuestaRecuperacion>




    //apis de favoritos

    // Obtener favoritos por id_usuario
    @GET("favoritos/obtener_favoritos.php")
    fun obtenerFavoritos(
        @Query("id_usuario") id_usuario: Int
    ): Call<dataRespuestaFavoritos>

    // Agregar favorito por id_usuario e id_producto
    @POST("favoritos/agregar_favoritos.php")
    fun agregarFavorito(
        @Body datos: dataPeticionFavorito
    ): Call<dataRespuestaOperacionFavorito>

    // Eliminar favorito por id_usuario e id_producto
    @POST("favoritos/eliminar_favoritos.php")
    fun eliminarFavorito(
        @Body datos: dataPeticionFavorito
    ): Call<dataRespuestaOperacionFavorito>
}