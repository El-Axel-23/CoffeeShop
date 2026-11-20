package com.example.coffeshop.Presentador

import com.example.coffeshop.Modelo.Usuario

interface PerfilView {
    fun mostrarCargando()
    fun ocultarCargando()
    fun mostrarPerfil(usuario: Usuario, iniciales: String)
    fun mostrarError(mensaje: String)
}