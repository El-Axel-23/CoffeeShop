
package com.example.coffeshop.Presentador

import android.util.Patterns

import com.example.coffeshop.Modelo.CrearCuentaModelo
import com.example.coffeshop.Modelo.RespuestaAutenticacion

import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearCuentaPresentador(private var vista: VistaCrearCuenta?) {
    // INTERFAZ DE LA VISTA
    interface VistaCrearCuenta {
        fun mostrarErrorNombre(mensaje: String)
        fun mostrarErrorCorreo(mensaje: String)
        fun mostrarErrorContrasena(mensaje: String)
        fun mostrarErrorConfirmacion(mensaje: String)
        fun mostrarErrorTerminos(mensaje: String)
        fun mostrarMensaje(mensaje: String)
        fun mostrarCargando(cargando: Boolean)
        fun cuentaCreada(mensaje: String)
    }
    private val modelo = CrearCuentaModelo()
    private var llamadaActiva: Call<RespuestaAutenticacion>? = null
    // REGISTRAR USUARIO
    fun registrar(
        nombreIngresado: String,
        correoIngresado: String,
        contrasena: String,
        confirmarContrasena: String,
        aceptoTerminos: Boolean
    ) {
        val nombre = nombreIngresado.trim()
        val correo = correoIngresado.trim()
        // VALIDAR NOMBRE
        if (
            nombre.isEmpty() ||
            nombre.toByteArray(Charsets.UTF_8).size > 120
        ) {
            vista?.mostrarErrorNombre("Ingresa un nombre completo válido.")
            return
        }
        // VALIDAR CORREO
        if (
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ||
            correo.toByteArray(Charsets.UTF_8).size > 120
        ) {
            vista?.mostrarErrorCorreo("Ingresa un correo electrónico válido.")
            return
        }
        // VALIDAR LONGITUD DE CONTRASEÑA
        val caracteres = contrasena.codePointCount(
            0,
            contrasena.length
        )
        if (caracteres < 15) {
            vista?.mostrarErrorContrasena("La contraseña debe contener al menos 15 caracteres.")
            return
        }
        // LIMITE TECNICO INTERNO
        if (
            contrasena.toByteArray(Charsets.UTF_8).size > 72
        ) {
            vista?.mostrarErrorContrasena("La contraseña supera la longitud permitida.")
            return
        }
        // CONFIRMAR CONTRASEÑA
        if (contrasena != confirmarContrasena) {
            vista?.mostrarErrorConfirmacion("Las contraseñas no coinciden.")
            return
        }
        // VALIDAR TERMINOS
        if (!aceptoTerminos) {
            vista?.mostrarErrorTerminos("Debes aceptar los términos y condiciones.")
            return
        }
        // EVITAR PETICIONES DUPLICADAS
        if (llamadaActiva != null) {
            return
        }
        vista?.mostrarCargando(true)
        // ENVIAR DATOS AL MODELO
        val peticion = modelo.crearCuenta(
            nombre,
            correo,
            contrasena,
            confirmarContrasena,
            aceptoTerminos
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
                    if (
                        response.isSuccessful &&
                        datos != null &&
                        datos.exito
                    ) {
                        // CUENTA REGISTRADA
                        vista?.cuentaCreada(
                            datos.mensaje
                        )
                    } else {
                        val mensaje = if (
                            datos != null && !datos.exito
                        ) {
                            datos.mensaje

                        } else {
                            leerMensajeError(response)
                        }
                        if (response.code() == 409) {
                            // CORREO DUPLICADO
                            vista?.mostrarErrorCorreo(
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

                    vista?.mostrarMensaje("No fue posible conectar con el servidor. Revisa tu conexión.")
                }
            }
        )
    }
    private fun leerMensajeError(
        response: Response<*>
    ): String {
        val alternativo = "Error del servidor (HTTP ${response.code()})."
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
