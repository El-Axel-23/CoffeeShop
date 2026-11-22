
package com.example.coffeshop.Vista

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeshop.R
import com.example.coffeshop.Presentador.RecuperarContrasenaPresentador

class recuperar_contrasena : AppCompatActivity(), RecuperarContrasenaPresentador.VistaRecuperarContrasena, View.OnClickListener {
    private lateinit var txtCorreoRecuperar: EditText
    private lateinit var txtErrorCorreo: TextView
    private lateinit var contenedorCorreo: LinearLayout
    private lateinit var btnEnviarEnlace: Button
    private lateinit var pantallaRecuperar: ScrollView
    private lateinit var pantallaCorreoEnviado: ScrollView
    private lateinit var txtReenviarCorreo: TextView
    private lateinit var presentador: RecuperarContrasenaPresentador
    private var ultimoCorreo = ""
    private var correoSolicitado = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)

        presentador = RecuperarContrasenaPresentador(this)
        txtCorreoRecuperar = findViewById(R.id.txtCorreoRecuperar)
        txtErrorCorreo = findViewById(R.id.txtErrorCorreo)
        contenedorCorreo = findViewById(R.id.contenedorCorreo)
        btnEnviarEnlace = findViewById(R.id.btnEnviarEnlace)
        pantallaRecuperar = findViewById(R.id.pantallaRecuperar)
        pantallaCorreoEnviado = findViewById(R.id.pantallaCorreoEnviado)
        txtReenviarCorreo = findViewById(R.id.txtReenviarCorreo)
        btnEnviarEnlace.setOnClickListener(this)
        txtReenviarCorreo.setOnClickListener(this)

        findViewById<View>(R.id.btnRegresar).setOnClickListener(this)
        findViewById<View>(R.id.txtVolverLogin).setOnClickListener(this)
        findViewById<View>(R.id.btnVolverIniciarSesion).setOnClickListener(this)
        // LIMPIAR ERROR CUANDO SE MODIFICA EL CORREO
        txtCorreoRecuperar.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    limpiarErrorCorreo()
                }
                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
        // RECUPERAR ESTADO AL GIRAR LA PANTALLA
        if (savedInstanceState != null) {
            ultimoCorreo = savedInstanceState.getString(
                "ultimoCorreo",
                ""
            )
            correoSolicitado = savedInstanceState.getBoolean(
                "correoSolicitado",
                false
            )
        }
        actualizarPantallas()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            // ENVIAR SOLICITUD
            R.id.btnEnviarEnlace -> {
                val correo = txtCorreoRecuperar.text
                    .toString()
                    .trim()
                ultimoCorreo = correo
                presentador.solicitarRecuperacion(correo)
            }
            // REENVIAR SOLICITUD
            R.id.txtReenviarCorreo -> {
                if (ultimoCorreo.isNotEmpty()) {
                    presentador.solicitarRecuperacion(ultimoCorreo)
                } else {
                    mostrarMensaje("No se encontró el correo para reenviar")
                }
            }
            // REGRESAR
            R.id.btnRegresar -> {
                if (correoSolicitado) {
                    correoSolicitado = false
                    actualizarPantallas()
                } else {
                    finish()
                }
            }
            // VOLVER AL LOGIN
            R.id.txtVolverLogin -> {
                irAlLogin()
            }
            // VOLVER AL LOGIN DESDE CONFIRMACION
            R.id.btnVolverIniciarSesion -> {
                irAlLogin()
            }
        }
    }
    // MOSTRAR ERROR EN EL CORREO
    override fun mostrarErrorCorreo(mensaje: String) {
        contenedorCorreo.setBackgroundResource(R.drawable.bg_campo_error)
        txtErrorCorreo.text = mensaje
        txtErrorCorreo.visibility = View.VISIBLE
        txtCorreoRecuperar.error = mensaje
        txtCorreoRecuperar.requestFocus()
    }
    // LIMPIAR ERROR
    override fun limpiarErrorCorreo() {
        contenedorCorreo.setBackgroundResource(R.drawable.bg_campo)
        txtErrorCorreo.visibility = View.GONE
        txtCorreoRecuperar.error = null
    }
    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
    // CONTROLAR CARGA
    override fun mostrarCargando(cargando: Boolean) {
        btnEnviarEnlace.isEnabled = !cargando
        txtReenviarCorreo.isEnabled = !cargando
        if (cargando) {
            btnEnviarEnlace.alpha = 0.6f
            txtReenviarCorreo.alpha = 0.6f

        } else {
            btnEnviarEnlace.alpha = 1.0f
            txtReenviarCorreo.alpha = 1.0f
        }
    }
    // SOLICITUD ACEPTADA POR EL SERVIDOR
    override fun solicitudCorrecta(mensaje: String) {
        mostrarMensaje(mensaje)
        correoSolicitado = true
        actualizarPantallas()
    }
    // CAMBIAR ENTRE PANTALLAS
    private fun actualizarPantallas() {
        if (correoSolicitado) {
            pantallaRecuperar.visibility = View.GONE
            pantallaCorreoEnviado.visibility = View.VISIBLE
        } else {
            pantallaRecuperar.visibility = View.VISIBLE
            pantallaCorreoEnviado.visibility = View.GONE
        }
    }
    // IR AL LOGIN
    private fun irAlLogin() {
        val intent = Intent(
            this,
            login::class.java
        )
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
        startActivity(intent)
        finish()
    }
    // GUARDAR ESTADO
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            "ultimoCorreo",
            ultimoCorreo
        )
        outState.putBoolean(
            "correoSolicitado",
            correoSolicitado
        )
        super.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        presentador.liberar()
        super.onDestroy()
    }
}