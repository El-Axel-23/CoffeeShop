package com.example.coffeshop.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeshop.Modelo.Usuario
import com.example.coffeshop.Presentador.PerfilPresenter
import com.example.coffeshop.R

class PerfilActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvIniciales: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var presenter: PerfilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        tvIniciales = findViewById(R.id.tvIniciales)
        tvNombre = findViewById(R.id.tvNombreUsuario)
        tvCorreo = findViewById(R.id.tvCorreoUsuario)

        findViewById<ImageView>(R.id.btnAtras).setOnClickListener { finish() }

        findViewById<TextView>(R.id.opUbicacion).setOnClickListener {
            startActivity(Intent(this, UbicacionActivity::class.java))
        }

        findViewById<TextView>(R.id.opAyuda).setOnClickListener {
            startActivity(Intent(this, AyudaActivity::class.java))
        }

        presenter = PerfilPresenter(this)
        presenter.cargarPerfil()
    }

    fun mostrarCargando() {
        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    fun ocultarCargando() {
        progressBar.visibility = View.GONE
    }

    fun mostrarPerfil(usuario: Usuario, iniciales: String) {
        tvIniciales.text = iniciales
        tvNombre.text = usuario.nombre_completo
        tvCorreo.text = usuario.correo
    }

    fun mostrarError(mensaje: String) {
        tvError.text = mensaje
        tvError.visibility = View.VISIBLE
    }
}