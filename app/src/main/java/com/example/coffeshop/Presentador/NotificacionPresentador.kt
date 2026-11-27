package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.NotificacionItem
import com.example.coffeshop.Vista.NotificacionActivity

class NotificacionPresentador(
    private val vista: NotificacionActivity
) {

    fun cargarNotificaciones() {
        // Datos de ejemplo mientras el endpoint de notificaciones queda listo en el servidor
        val listaEjemplo = listOf(
            NotificacionItem(
                id = 1,
                mensaje = "Tu pedido #1024 está en preparación",
                tiempo = "hace 5 min",
                leida = false
            ),
            NotificacionItem(
                id = 2,
                mensaje = "Tu pedido #1024 está en preparación",
                tiempo = "hace 5 min",
                leida = false
            ),
            NotificacionItem(
                id = 3,
                mensaje = "Tu pedido #1024 está en preparación",
                tiempo = "hace 5 min",
                leida = true
            )
        )

        vista.mostrarNotificaciones(listaEjemplo)

        // Cuando conecten el endpoint con Retrofit, aquí iría la llamada asíncrona
        // y al recibir la respuesta llamarías a vista.mostrarNotificaciones(lista)
        // o vista.mostrarMensaje("No se pudieron cargar las notificaciones") si falla.
    }

    fun onNotificacionClick(notificacion: NotificacionItem) {
        vista.mostrarMensaje(notificacion.mensaje)
    }
}