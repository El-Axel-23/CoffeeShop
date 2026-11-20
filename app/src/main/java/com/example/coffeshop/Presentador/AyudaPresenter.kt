package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Modelo.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AyudaPresenter(private val view: AyudaView) {

    fun cargarPreguntas() {
        view.mostrarCargando()

        RetrofitClient.api.obtenerPreguntas().enqueue(object : Callback<RespuestaApi<List<PreguntaFrecuente>>> {

            override fun onResponse(call: Call<RespuestaApi<List<PreguntaFrecuente>>>, response: Response<RespuestaApi<List<PreguntaFrecuente>>>) {
                view.ocultarCargando()
                if (response.isSuccessful) {
                    procesarRespuesta(response.body())
                } else {
                    view.mostrarError("Error del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RespuestaApi<List<PreguntaFrecuente>>>, t: Throwable) {
                view.ocultarCargando()
                view.mostrarError("Sin conexión. Revisa tu internet")
            }
        })
    }

    fun procesarRespuesta(respuesta: RespuestaApi<List<PreguntaFrecuente>>?) {
        if (respuesta == null) {
            view.mostrarError("No se recibió respuesta")
        } else if (respuesta.status != "success") {
            view.mostrarError(respuesta.message)
        } else if (respuesta.data == null || respuesta.data.isEmpty()) {
            view.mostrarError("No hay preguntas disponibles")
        } else {
            view.mostrarPreguntas(respuesta.data)
        }
    }
}