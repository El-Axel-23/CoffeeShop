package com.example.coffeshop.Modelo

import retrofit2.Call

class RecuperarContrasenaModelo {
    fun solicitarRecuperacion(
        correo: String
    ): Call<RespuestaRecuperacion> {

        val datos = DatosRecuperarContrasena(
            correo = correo
        )
        return RetrofitCliente.api.recuperarContrasena(datos)
    }
}