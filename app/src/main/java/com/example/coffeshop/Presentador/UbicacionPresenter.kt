package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.Cafeteria
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Redes.RetrofitClient
import com.example.coffeshop.Vista.UbicacionActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UbicacionPresenter(private val view: UbicacionActivity) {

    fun cargarCafeteria() {
        view.mostrarCargando()

        RetrofitClient.api.obtenerCafeteria().enqueue(object : Callback<RespuestaApi<List<Cafeteria>>> {

            override fun onResponse(call: Call<RespuestaApi<List<Cafeteria>>>, response: Response<RespuestaApi<List<Cafeteria>>>) {
                view.ocultarCargando()
                if (response.isSuccessful) {
                    procesarRespuesta(response.body())
                } else {
                    view.mostrarError("Error del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RespuestaApi<List<Cafeteria>>>, t: Throwable) {
                view.ocultarCargando()
                view.mostrarError("Sin conexión. Revisa tu internet")
            }
        })
    }

    fun procesarRespuesta(respuesta: RespuestaApi<List<Cafeteria>>?) {
        if (respuesta == null) {
            view.mostrarError("No se recibió respuesta")
        } else if (respuesta.status != "success") {
            view.mostrarError(respuesta.message)
        } else if (respuesta.data == null || respuesta.data.isEmpty()) {
            view.mostrarError("No hay información de la cafetería")
        } else {
            val cafeteria = respuesta.data[0]
            view.mostrarCafeteria(cafeteria, armarHorario(cafeteria))
        }
    }

    companion object {
        fun armarHorario(c: Cafeteria): String {
            return c.dias_atencion + ": " + c.hora_apertura.take(5) + " a " + c.hora_cierre.take(5)
        }
    }
}