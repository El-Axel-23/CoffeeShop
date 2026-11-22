package com.example.coffeshop.Presentador

import android.util.Patterns

import com.example.coffeshop.Modelo.LoginModelo
import com.example.coffeshop.Modelo.RespuestaAutenticacion
import com.example.coffeshop.Modelo.UsuarioAutenticacion

import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresentador(private var vista: VistaLogin?) {
    interface VistaLogin {
        fun mostrarErrorCorreo(mensaje: String)
        fun mostrarErrorContrasena(mensaje: String)
        fun mostrarMensaje(mensaje: String)
        fun mostrarCargando(cargando: Boolean)
        fun inicioCorrecto(usuario: UsuarioAutenticacion)
    }
    private val modelo = LoginModelo()
    private var llamadaActiva: Call<RespuestaAutenticacion>? = null
    // INICIAR SESION
    fun iniciarSesion(
        correoIngresado: String,
        contrasena: String
    ) {
        val correo = correoIngresado.trim()
        // VALIDAR CORREO
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            vista?.mostrarErrorCorreo(
                "Ingresa un correo electrónico válido."
            )
            return
        }
        // VALIDAR CONTRASEÑA
        if (contrasena.isEmpty()) {
            vista?.mostrarErrorContrasena(
                "Ingresa tu contraseña."
            )
            return
        }
        // EVITAR PETICIONES DUPLICADAS
        if (llamadaActiva != null) {
            return
        }
        vista?.mostrarCargando(true)
        // LLAMAR AL MODELO
        val peticion = modelo.iniciarSesion(
            correo,
            contrasena
        )
        llamadaActiva = peticion
        peticion.enqueue(
            object : Callback<RespuestaAutenticacion> {
                // RESPUESTA DEL SERVIDOR
                override fun onResponse(
                    call: Call<RespuestaAutenticacion>,
                    response: Response<RespuestaAutenticacion>
                ) {
                    if (call.isCanceled) {
                        return
                    }
                    llamadaActiva = null
                    vista?.mostrarCargando(false)
                    val datos = response.body()
                    if (response.isSuccessful && datos != null) {
                        if (datos.exito && datos.usuario != null) {
                            // LOGIN CORRECTO
                            vista?.inicioCorrecto(
                                datos.usuario
                            )
                        } else {
                            vista?.mostrarMensaje(
                                datos.mensaje
                            )
                        }
                    } else {
                        val mensaje = leerMensajeError(response)
                        if (response.code() == 401) {
                            // CREDENCIALES INCORRECTAS
                            vista?.mostrarErrorContrasena(
                                mensaje
                            )
                        } else {
                            vista?.mostrarMensaje(
                                mensaje
                            )
                        }
                    }
                }
                // ERROR DE CONEXION
                override fun onFailure(
                    call: Call<RespuestaAutenticacion>,
                    t: Throwable
                ) {
                    if (call.isCanceled) {
                        return
                    }
                    llamadaActiva = null
                    vista?.mostrarCargando(false)
                    vista?.mostrarMensaje(
                        "No fue posible conectar con el servidor. Revisa tu conexión."
                    )
                }
            }
        )
    }
    private fun leerMensajeError(
        response: Response<*>
    ): String {
        val alternativo =
            "Error del servidor (HTTP ${response.code()})."
        return try {
            val texto = response.errorBody()?.string()
            if (texto.isNullOrBlank()) {
                alternativo
            } else {
                JSONObject(texto).optString(
                    "mensaje",
                    alternativo
                )
            }

        } catch (e: Exception) {

            alternativo
        }
    }
    fun liberar() {
        llamadaActiva?.cancel()
        llamadaActiva = null
        vista = null
    }
}