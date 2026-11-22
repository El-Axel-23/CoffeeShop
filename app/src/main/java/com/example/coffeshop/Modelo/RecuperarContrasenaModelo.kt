
package com.example.coffeshop.Modelo

import retrofit2.Call

class RecuperarContrasenaModelo {
    fun solicitarRecuperacion(
        correo: String
    ): Call<RespuestaRecuperacion> {
        val datos = DatosRecuperarContrasena(
            accion = "solicitar",
            correo = correo
        )
        return RetrofitCliente.api.recuperarContrasena(datos)
    }
}