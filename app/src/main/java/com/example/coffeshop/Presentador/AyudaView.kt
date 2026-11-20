package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.PreguntaFrecuente

interface AyudaView {
    fun mostrarCargando()
    fun ocultarCargando()
    fun mostrarPreguntas(lista: List<PreguntaFrecuente>)
    fun mostrarError(mensaje: String)
}