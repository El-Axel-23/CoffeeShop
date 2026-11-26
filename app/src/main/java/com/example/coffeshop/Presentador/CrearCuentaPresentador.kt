package com.example.coffeshop.Presentador

import android.util.Patterns
import com.example.coffeshop.Modelo.CrearCuentaModelo
import com.example.coffeshop.Modelo.RespuestaAutenticacion
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearCuentaPresentador(private var vista: VistaCrearCuenta?) {

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

    // Registrar usuario
    fun registrar(
        nombreIngresado: String,
        correoIngresado: String,
        contrasena: String,
        confirmarContrasena: String,
        aceptoTerminos: Boolean
    ) {
        val nombre = nombreIngresado.trim()
        val correo = correoIngresado.trim()

        // Validar nombre
        if (
            nombre.isEmpty() ||
            nombre.codePointCount(0, nombre.length) > 120
        ) {
            vista?.mostrarErrorNombre("Ingresa un nombre completo válido.")
            return
        }

        // Validar correo
        if (
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ||
            correo.toByteArray(Charsets.UTF_8).size > 120
        ) {
            vista?.mostrarErrorCorreo("Ingresa un correo electrónico válido.")
            return
        }

        // Validar longitud de contraseña
        val caracteres = contrasena.codePointCount(
            0,
            contrasena.length
        )

        if (caracteres < 15) {
            vista?.mostrarErrorContrasena("La contraseña debe contener al menos 15 caracteres.")
            return
        }

        // Validar limite de bcrypt
        if (contrasena.toByteArray(Charsets.UTF_8).size > 72) {
            vista?.mostrarErrorContrasena("La contraseña supera la longitud permitida.")
            return
        }

        // Validar caracter nulo
        if (contrasena.indexOf('\u0000') >= 0) {
            vista?.mostrarErrorContrasena("La contraseña contiene un carácter no válido.")
            return
        }

        // Confirmar contraseña
        if (contrasena != confirmarContrasena) {
            vista?.mostrarErrorConfirmacion("Las contraseñas no coinciden.")
            return
        }

        // Validar terminos
        if (!aceptoTerminos) {
            vista?.mostrarErrorTerminos("Debes aceptar los términos y condiciones.")
            return
        }

        // Evitar peticiones duplicadas
        if (llamadaActiva != null) {
            return
        }

        vista?.mostrarCargando(true)

        // Enviar datos al modelo
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
                        // Cuenta registrada
                        vista?.cuentaCreada(datos.mensaje)

                    } else {

                        val mensaje = if (
                            datos != null && !datos.exito
                        ) {
                            datos.mensaje
                        } else {
                            leerMensajeError(response)
                        }

                        if (response.code() == 409) {
                            // Correo duplicado
                            vista?.mostrarErrorCorreo(mensaje)

                        } else {
                            vista?.mostrarMensaje(mensaje)
                        }
                    }
                }
                override fun onFailure(
                    call: Call<RespuestaAutenticacion>,
                    t: Throwable
                ) {
                    if (call.isCanceled) {
                        return
                    }

                    llamadaActiva = null
                    vista?.mostrarCargando(false)
                    vista?.mostrarMensaje("No fue posible conectar con el servidor. Revisa tu conexión."
                    )
                }
            }
        )
    }

    // Obtener mensaje de error del PHP
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

    // Liberar la vista
    fun liberar() {
        llamadaActiva?.cancel()
        llamadaActiva = null
        vista = null
    }
}