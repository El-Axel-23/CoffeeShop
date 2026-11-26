package com.example.coffeshop.Vista

import android.content.Intent
import android.graphics.Paint
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
import androidx.core.view.WindowCompat
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
    private lateinit var txtVolverLogin: TextView
    private lateinit var presentador: RecuperarContrasenaPresentador
    private var ultimoCorreo = ""
    private var correoSolicitado = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)
        ajustarPantalla()
        presentador = RecuperarContrasenaPresentador(this)
        txtCorreoRecuperar = findViewById(R.id.txtCorreoRecuperar)
        txtErrorCorreo = findViewById(R.id.txtErrorCorreo)
        contenedorCorreo = findViewById(R.id.contenedorCorreo)
        btnEnviarEnlace = findViewById(R.id.btnEnviarEnlace)
        pantallaRecuperar = findViewById(R.id.pantallaRecuperar)
        pantallaCorreoEnviado = findViewById(R.id.pantallaCorreoEnviado)
        txtReenviarCorreo = findViewById(R.id.txtReenviarCorreo)
        txtVolverLogin = findViewById(R.id.txtVolverLogin)
        // Subrayar los enlaces
        txtVolverLogin.paintFlags = txtVolverLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        txtReenviarCorreo.paintFlags = txtReenviarCorreo.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        // Eventos de los botones
        btnEnviarEnlace.setOnClickListener(this)
        txtReenviarCorreo.setOnClickListener(this)
        txtVolverLogin.setOnClickListener(this)
        findViewById<View>(R.id.btnRegresar).setOnClickListener(this)
        findViewById<View>(R.id.btnVolverIniciarSesion).setOnClickListener(this)
        // Limpiar error cuando se modifica el correo
        txtCorreoRecuperar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
                }
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    limpiarErrorCorreo()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
        )
        // Recuperar estado al girar la pantalla
        if (savedInstanceState != null) {
            ultimoCorreo = savedInstanceState.getString("ultimoCorreo", "")
            correoSolicitado = savedInstanceState.getBoolean("correoSolicitado", false)
        }
        actualizarPantallas()
        // Evitar que el teclado se abra automáticamente
        if (savedInstanceState == null) {
            findViewById<View>(R.id.main).requestFocus()
        }
    }
    // Ajustar el contenido a las barras y al teclado
    private fun ajustarPantalla() {
        val principal = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(principal, object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(vista: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val espacios = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars() or
                                WindowInsetsCompat.Type.displayCutout() or
                                WindowInsetsCompat.Type.ime()
                    )
                    vista.setPadding(
                        espacios.left,
                        espacios.top,
                        espacios.right,
                        espacios.bottom
                    )
                    return WindowInsetsCompat.CONSUMED
                }
            }
        )
        ViewCompat.requestApplyInsets(principal)
        // Iconos oscuros en las barras
        val controlador = WindowCompat.getInsetsController(window, window.decorView)
        controlador.isAppearanceLightStatusBars = true
        controlador.isAppearanceLightNavigationBars = true
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            // Enviar solicitud
            R.id.btnEnviarEnlace -> {
                val correo = txtCorreoRecuperar.text.toString().trim()
                ultimoCorreo = correo
                presentador.solicitarRecuperacion(correo)
            }
            // Reenviar solicitud
            R.id.txtReenviarCorreo -> {
                if (ultimoCorreo.isNotEmpty()) {
                    presentador.solicitarRecuperacion(ultimoCorreo)
                } else {
                    mostrarMensaje("No se encontró el correo para reenviar")
                }
            }
            R.id.btnRegresar -> {
                if (correoSolicitado) {
                    correoSolicitado = false
                    actualizarPantallas()
                } else {
                    finish()
                }
            }
            // Volver al Login
            R.id.txtVolverLogin -> {
                irAlLogin()
            }
            // Volver al Login desde confirmación
            R.id.btnVolverIniciarSesion -> {
                irAlLogin()
            }
        }
    }
    // Cambiar el fondo sin modificar los espacios del campo
    private fun cambiarFondoCorreo(recurso: Int) {
        val izquierda = contenedorCorreo.paddingStart
        val arriba = contenedorCorreo.paddingTop
        val derecha = contenedorCorreo.paddingEnd
        val abajo = contenedorCorreo.paddingBottom
        contenedorCorreo.setBackgroundResource(recurso)
        contenedorCorreo.setPaddingRelative(
            izquierda,
            arriba,
            derecha,
            abajo
        )
    }
    // Mostrar error en el correo
    override fun mostrarErrorCorreo(mensaje: String) {
        cambiarFondoCorreo(R.drawable.bg_campo_error)
        txtErrorCorreo.text = mensaje
        txtErrorCorreo.visibility = View.VISIBLE
        txtCorreoRecuperar.requestFocus()
    }
    // Limpiar error
    override fun limpiarErrorCorreo() {
        cambiarFondoCorreo(R.drawable.bg_campo)
        txtErrorCorreo.visibility = View.GONE
        txtErrorCorreo.error = null
    }
    // Mostrar mensajes
    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
    // Controlar carga durante la petición
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
    // Solicitud aceptada por el servidor
    override fun solicitudCorrecta(mensaje: String) {
        mostrarMensaje(mensaje)
        correoSolicitado = true
        ocultarTeclado()
        actualizarPantallas()
    }
    // Ocultar el teclado
    private fun ocultarTeclado() {
        val controlador = WindowCompat.getInsetsController(window, window.decorView)
        controlador.hide(WindowInsetsCompat.Type.ime())
        txtCorreoRecuperar.clearFocus()
        findViewById<View>(R.id.main).requestFocus()
    }
    // Cambiar entre pantallas
    private fun actualizarPantallas() {
        if (correoSolicitado) {
            pantallaRecuperar.visibility = View.GONE
            pantallaCorreoEnviado.visibility = View.VISIBLE
        } else {
            pantallaRecuperar.visibility = View.VISIBLE
            pantallaCorreoEnviado.visibility = View.GONE
        }
    }
    // Ir al Login
    private fun irAlLogin() {
        ocultarTeclado()
        val siguiente = Intent(this, login::class.java)
        siguiente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(siguiente)
        finish()
    }
    // Guardar estado
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ultimoCorreo", ultimoCorreo)
        outState.putBoolean("correoSolicitado", correoSolicitado)
        super.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.liberar()
        }
        super.onDestroy()
    }
}