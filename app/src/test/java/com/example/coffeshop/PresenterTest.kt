package com.example.coffeshop

import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Modelo.RespuestaApi
import com.example.coffeshop.Modelo.Usuario
import com.example.coffeshop.Presentador.AyudaPresenter
import com.example.coffeshop.Presentador.AyudaView
import com.example.coffeshop.Presentador.PerfilPresenter
import com.example.coffeshop.Presentador.PerfilView
import org.junit.Assert.assertEquals
import org.junit.Test

class PresenterTest {

    class PerfilFalsa : PerfilView {
        var error = ""
        var iniciales = ""
        override fun mostrarCargando() {}
        override fun ocultarCargando() {}
        override fun mostrarPerfil(usuario: Usuario, iniciales: String) { this.iniciales = iniciales }
        override fun mostrarError(mensaje: String) { error = mensaje }
    }

    class AyudaFalsa : AyudaView {
        var error = ""
        var cantidad = 0
        override fun mostrarCargando() {}
        override fun ocultarCargando() {}
        override fun mostrarPreguntas(lista: List<PreguntaFrecuente>) { cantidad = lista.size }
        override fun mostrarError(mensaje: String) { error = mensaje }
    }

    @Test
    fun perfil_conStatusError_muestraElMensaje() {
        val vista = PerfilFalsa()
        val respuesta = RespuestaApi<Usuario>("error", "No se encontro el usuario", null)
        PerfilPresenter(vista).procesarRespuesta(respuesta)
        assertEquals("No se encontro el usuario", vista.error)
    }

    @Test
    fun perfil_conDatos_calculaIniciales() {
        val vista = PerfilFalsa()
        val usuario = Usuario(1, "Cliente de ejemplo", "cliente@example.com", "7710000000")
        PerfilPresenter(vista).procesarRespuesta(RespuestaApi("success", "ok", usuario))
        assertEquals("CE", vista.iniciales)
    }

    @Test
    fun ayuda_listaVacia_muestraError() {
        val vista = AyudaFalsa()
        AyudaPresenter(vista).procesarRespuesta(RespuestaApi("success", "ok", emptyList()))
        assertEquals("No hay preguntas disponibles", vista.error)
    }

    @Test
    fun ayuda_conPreguntas_lasMuestra() {
        val vista = AyudaFalsa()
        val lista = listOf(PreguntaFrecuente(1, "¿Cómo pago?", "En la cafetería"))
        AyudaPresenter(vista).procesarRespuesta(RespuestaApi("success", "ok", lista))
        assertEquals(1, vista.cantidad)
    }

    @Test
    fun perfil_conUnSoloNombre_tomaDosLetras() {
        val vista = PerfilFalsa()
        val usuario = Usuario(1, "Axel", "axel@example.com", "742617000027")
        PerfilPresenter(vista).procesarRespuesta(RespuestaApi("success", "ok", usuario))
        assertEquals("AX", vista.iniciales)
    }
}