package com.example.coffeshop.Vista


import com.example.coffeshop.R
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Adaptador.NotificacionAdapter
import com.example.coffeshop.Modelo.NotificacionItem
import com.example.coffeshop.Presentador.NotificacionPresentador

class NotificacionActivity : AppCompatActivity() {

    private lateinit var ivVolver: ImageView
    private lateinit var rvNotificaciones: RecyclerView
    private lateinit var tvSinNotificaciones: TextView

    private lateinit var adaptador: NotificacionAdapter
    private lateinit var presentador: NotificacionPresentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacion)

        inicializarVistas()
        configurarRecyclerView()
        configurarEventos()

        presentador = NotificacionPresentador(this)
        presentador.cargarNotificaciones()
    }

    private fun inicializarVistas() {
        ivVolver = findViewById(R.id.ivVolver)
        rvNotificaciones = findViewById(R.id.rvNotificaciones)
        tvSinNotificaciones = findViewById(R.id.tvSinNotificaciones)
    }

    private fun configurarRecyclerView() {
        adaptador = NotificacionAdapter(emptyList()) { notificacion ->
            presentador.onNotificacionClick(notificacion)
        }
        rvNotificaciones.layoutManager = LinearLayoutManager(this)
        rvNotificaciones.adapter = adaptador
    }

    private fun configurarEventos() {
        ivVolver.setOnClickListener {
            finish()
        }
    }

    // Métodos que llama NotificacionPresentador hacia la Vista
    fun mostrarNotificaciones(lista: List<NotificacionItem>) {
        if (lista.isEmpty()) {
            mostrarEstadoVacio(true)
        } else {
            mostrarEstadoVacio(false)
            adaptador.actualizarLista(lista)
        }
    }

    fun mostrarEstadoVacio(vacio: Boolean) {
        tvSinNotificaciones.visibility = if (vacio) View.VISIBLE else View.GONE
        rvNotificaciones.visibility = if (vacio) View.GONE else View.VISIBLE
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
    }