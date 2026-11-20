package com.example.coffeshop.Vista

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Adaptador.PreguntaAdapter
import com.example.coffeshop.Modelo.PreguntaFrecuente
import com.example.coffeshop.Presentador.AyudaPresenter
import com.example.coffeshop.Presentador.AyudaView
import com.example.coffeshop.R

class AyudaActivity : AppCompatActivity(), AyudaView {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var rvPreguntas: RecyclerView
    private lateinit var presenter: AyudaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)

        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        rvPreguntas = findViewById(R.id.rvPreguntas)
        rvPreguntas.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.btnAtras).setOnClickListener { finish() }

        presenter = AyudaPresenter(this)
        presenter.cargarPreguntas()
    }

    override fun mostrarCargando() {
        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE
        rvPreguntas.visibility = View.GONE
    }

    override fun ocultarCargando() {
        progressBar.visibility = View.GONE
    }

    override fun mostrarPreguntas(lista: List<PreguntaFrecuente>) {
        rvPreguntas.visibility = View.VISIBLE
        rvPreguntas.adapter = PreguntaAdapter(lista)
    }

    override fun mostrarError(mensaje: String) {
        tvError.text = mensaje
        tvError.visibility = View.VISIBLE
        rvPreguntas.visibility = View.GONE
    }
}