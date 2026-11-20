package com.example.coffeshop.Interface

import com.example.coffeshop.Modelo.Cafeteria
import com.example.coffeshop.Modelo.Direccion
import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Modelo.Usuario
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("perfil.php")
    fun obtenerPerfil(): Call<RespuestaApi<Usuario>>

    @GET("ubicacion.php")
    fun obtenerCafeteria(): Call<RespuestaApi<List<Cafeteria>>>

    @GET("preguntas.php")
    fun obtenerPreguntas(): Call<RespuestaApi<List<PreguntaFrecuente>>>

    @GET("direcciones.php")
    fun obtenerDirecciones(): Call<RespuestaApi<List<Direccion>>>
}