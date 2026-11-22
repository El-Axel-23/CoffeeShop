
package com.example.coffeshop.Presentador

import android.util.Patterns
import com.example.coffeshop.Modelo.RecuperarContrasenaModelo
import com.example.coffeshop.Modelo.RespuestaRecuperacion
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecuperarContrasenaPresentador(private var vista: VistaRecuperarContrasena?) {
    interface VistaRecuperarContrasena {
        fun mostrarErrorCorreo(mensaje: String)
        fun limpiarErrorCorreo()
        fun mostrarMensaje(mensaje: String)
        fun mostrarCargando(cargando: Boolean)
        fun solicitudCorrecta(mensaje: String)
    }
    private val modelo = RecuperarContrasenaModelo()
    private var llamadaActiva: Call<RespuestaRecuperacion>? = null
    // SOLICITAR RECUPERACION
    fun solicitarRecuperacion(correo: String) {
        if (llamadaActiva != null) {
            return
        }
        val correoLimpio = correo.trim()
        vista?.limpiarErrorCorreo()
        // VALIDAR CORREO VACIO
        if (correoLimpio.isEmpty()) {
            vista?.mostrarErrorCorreo(
                "Ingresa tu correo electrónico"
            )
            return
        }
        // VALIDAR FORMATO DEL CORREO
        if (!Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches()) {
            vista?.mostrarErrorCorreo(
                "Ingresa un correo válido"
            )
            return
        }
        // MOSTRAR CARGA
        vista?.mostrarCargando(true)

        // LLAMAR AL MODELO
        val llamada = modelo.solicitarRecuperacion(
            correoLimpio
        )
        llamadaActiva = llamada
        llamada.enqueue(
            object : Callback<RespuestaRecuperacion> {
                override fun onResponse(
                    call: Call<RespuestaRecuperacion>,
                    response: Response<RespuestaRecuperacion>
                ) {
                    if (call.isCanceled) {
                        return
                    }
                    llamadaActiva = null
                    vista?.mostrarCargando(false)
                    // VERIFICAR ESTADO HTTP
                    if (!response.isSuccessful) {
                        vista?.mostrarMensaje(
                            obtenerMensajeError(response)
                        )
                        return
                    }
                    // OBTENER RESPUESTA
                    val resultado = response.body()
                    if (resultado == null) {
                        vista?.mostrarMensaje(
                            "No se recibió respuesta del servidor"
                        )
                        return
                    }
                    // VERIFICAR RESULTADO
                    if (resultado.exito) {
                        vista?.solicitudCorrecta(
                            resultado.mensaje
                        )
                    } else {
                        vista?.mostrarMensaje(
                            resultado.mensaje
                        )
                    }
                }
                override fun onFailure(
                    call: Call<RespuestaRecuperacion>,
                    t: Throwable
                ) {
                    if (call.isCanceled) {
                        return
                    }
                    llamadaActiva = null
                    vista?.mostrarCargando(false)
                    vista?.mostrarMensaje(
                        "No se pudo conectar con el servidor. " +
                                "Revisa tu conexión a internet."
                    )
                }
            }
        )
    }
    private fun obtenerMensajeError(
        response: Response<RespuestaRecuperacion>
    ): String {
        val mensajePredeterminado =
            "Error del servidor (${response.code()})"
        return try {
            val contenido = response.errorBody()?.string()
            if (contenido.isNullOrBlank()) {
                mensajePredeterminado
            } else {
                JSONObject(contenido).optString(
                    "mensaje",
                    mensajePredeterminado
                ).ifBlank {
                    mensajePredeterminado
                }
            }
        } catch (e: Exception) {
            mensajePredeterminado
        }
    }
    fun liberar() {
        vista = null
        llamadaActiva?.cancel()
        llamadaActiva = null
    }
}