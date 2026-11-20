package com.example.coffeshop

import com.example.coffeshop.Modelo.Cafeteria
import com.example.coffeshop.Presentador.PerfilPresenter
import com.example.coffeshop.Presentador.UbicacionPresenter
import org.junit.Assert.assertEquals
import org.junit.Test

class PresenterTest {

    @Test
    fun iniciales_nombreCompleto() {
        assertEquals("CE", PerfilPresenter.obtenerIniciales("Cliente de ejemplo"))
    }

    @Test
    fun iniciales_unSoloNombre() {
        assertEquals("AX", PerfilPresenter.obtenerIniciales("Axel"))
    }

    @Test
    fun horario_quitaLosSegundos() {
        val cafeteria = Cafeteria(
            "Cafetería Central", "Dirección de prueba", 20.0, -98.0,
            "1234567890", "cafeteria@gmail.com",
            "Lunes a Viernes", "07:00:00", "18:00:00"
        )
        assertEquals("Lunes a Viernes: 07:00 a 18:00", UbicacionPresenter.armarHorario(cafeteria))
    }
}