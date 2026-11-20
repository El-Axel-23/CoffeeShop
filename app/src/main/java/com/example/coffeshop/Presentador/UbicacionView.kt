package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.Cafeteria

interface UbicacionView {
    fun mostrarCargando()
    fun ocultarCargando()
    fun mostrarCafeteria(cafeteria: Cafeteria, horario: String)
    fun mostrarError(mensaje: String)
}