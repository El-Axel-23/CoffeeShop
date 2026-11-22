
package com.example.coffeshop.Modelo

import retrofit2.Call

class CrearCuentaModelo {
    fun crearCuenta(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String,
        aceptoTerminos: Boolean
    ): Call<RespuestaAutenticacion> {

        val datos = DatosCrearCuenta(
            nombre_completo = nombre,
            correo = correo,
            contrasena = contrasena,
            confirmar_contrasena = confirmarContrasena,
            acepto_terminos = aceptoTerminos
        )
        return RetrofitCliente.api.crearCuenta(datos)
    }
}