
package com.example.coffeshop.Modelo

import retrofit2.Call

class LoginModelo {
    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Call<RespuestaAutenticacion> {
        val datos = DatosLogin(
            correo = correo,
            contrasena = contrasena
        )
        return RetrofitCliente.api.iniciarSesion(datos)
    }
}