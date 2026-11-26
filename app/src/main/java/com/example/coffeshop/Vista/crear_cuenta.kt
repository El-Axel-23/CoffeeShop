package com.example.coffeshop.Vista

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

import com.example.coffeshop.R
import com.example.coffeshop.Presentador.CrearCuentaPresentador

class crear_cuenta : AppCompatActivity(), CrearCuentaPresentador.VistaCrearCuenta, View.OnClickListener {
    private lateinit var presentador: CrearCuentaPresentador
    private lateinit var txtNombreCompleto: EditText
    private lateinit var txtCorreoRegistro: EditText
    private lateinit var txtContrasenaRegistro: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var chkTerminos: CheckBox
    private lateinit var btnCrearCuenta: View
    private lateinit var btnVerContrasenaRegistro: ImageButton
    private lateinit var btnVerConfirmar: ImageButton
    private lateinit var txtIrLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)
        ajustarPantalla()
        presentador = CrearCuentaPresentador(this)
        txtNombreCompleto = findViewById(R.id.txtNombreCompleto)
        txtCorreoRegistro = findViewById(R.id.txtCorreoRegistro)
        txtContrasenaRegistro = findViewById(R.id.txtContrasenaRegistro)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        chkTerminos = findViewById(R.id.chkTerminos)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)
        btnVerContrasenaRegistro = findViewById(R.id.btnVerContrasenaRegistro)
        btnVerConfirmar = findViewById(R.id.btnVerConfirmar)
        txtIrLogin = findViewById(R.id.txtIrLogin)
        // Ocultar las contraseñas al iniciar
        txtContrasenaRegistro.transformationMethod = PasswordTransformationMethod.getInstance()
        txtConfirmarContrasena.transformationMethod = PasswordTransformationMethod.getInstance()
        btnVerContrasenaRegistro.setImageResource(R.drawable.ic_ojo_cerrado)
        btnVerConfirmar.setImageResource(R.drawable.ic_ojo_cerrado)
        btnVerContrasenaRegistro.contentDescription = "Mostrar contraseña"
        btnVerConfirmar.contentDescription = "Mostrar contraseña"
        // Subrayar el enlace de iniciar sesión
        txtIrLogin.paintFlags = txtIrLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        btnCrearCuenta.setOnClickListener(this)
        btnVerContrasenaRegistro.setOnClickListener(this)
        btnVerConfirmar.setOnClickListener(this)
        txtIrLogin.setOnClickListener(this)
        findViewById<View>(R.id.btnRegresar).setOnClickListener(this)
        findViewById<View>(R.id.btnRegistroGoogle).setOnClickListener(this)
        findViewById<View>(R.id.btnRegistroApple).setOnClickListener(this)
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
                    val espacios = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())
                    vista.setPadding(espacios.left, espacios.top, espacios.right, espacios.bottom)
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
            // Crear cuenta
            R.id.btnCrearCuenta -> {
                presentador.registrar(
                    txtNombreCompleto.text.toString(),
                    txtCorreoRegistro.text.toString(),
                    txtContrasenaRegistro.text.toString(),
                    txtConfirmarContrasena.text.toString(),
                    chkTerminos.isChecked
                )
            }
            // Mostrar u ocultar contraseña
            R.id.btnVerContrasenaRegistro -> {
                cambiarVisibilidad(txtContrasenaRegistro, btnVerContrasenaRegistro)
            }
            // Mostrar u ocultar confirmación
            R.id.btnVerConfirmar -> {
                cambiarVisibilidad(txtConfirmarContrasena, btnVerConfirmar)
            }
            // Regresar
            R.id.btnRegresar -> {
                finish()
            }
            // Ir al Login
            R.id.txtIrLogin -> {
                volverAlLogin()
            }
            // Registro con Google
            R.id.btnRegistroGoogle -> {
                mostrarMensaje("Registro con Google aún no configurado")
            }
            // Registro con Apple
            R.id.btnRegistroApple -> {
                mostrarMensaje("Registro con Apple aún no configurado")
            }
        }
    }
    // Mostrar y ocultar contraseñas
    private fun cambiarVisibilidad(campo: EditText, boton: ImageButton) {
        if (campo.transformationMethod is PasswordTransformationMethod) {
            // Mostrar contraseña
            campo.transformationMethod = HideReturnsTransformationMethod.getInstance()
            boton.setImageResource(R.drawable.ic_ojo)
            boton.contentDescription = "Ocultar contraseña"
        } else {
            // Ocultar contraseña
            campo.transformationMethod = PasswordTransformationMethod.getInstance()
            boton.setImageResource(R.drawable.ic_ojo_cerrado)
            boton.contentDescription = "Mostrar contraseña"
        }
        // Mantener el cursor al final
        campo.setSelection(campo.text.length)
    }
    // Error en nombre
    override fun mostrarErrorNombre(mensaje: String) {
        txtNombreCompleto.error = mensaje
        txtNombreCompleto.requestFocus()
    }
    // Error en correo
    override fun mostrarErrorCorreo(mensaje: String) {
        txtCorreoRegistro.error = mensaje
        txtCorreoRegistro.requestFocus()
    }
    // Error en contraseña
    override fun mostrarErrorContrasena(mensaje: String) {
        txtContrasenaRegistro.error = mensaje
        txtContrasenaRegistro.requestFocus()
    }
    // Error al confirmar contraseña
    override fun mostrarErrorConfirmacion(mensaje: String) {
        txtConfirmarContrasena.error = mensaje
        txtConfirmarContrasena.requestFocus()
    }
    // Error de términos y condiciones
    override fun mostrarErrorTerminos(mensaje: String) {
        mostrarMensaje(mensaje)
    }
    // Mostrar mensajes
    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
    // Controlar el botón durante la petición
    override fun mostrarCargando(cargando: Boolean) {
        btnCrearCuenta.isEnabled = !cargando
        if (cargando) {
            btnCrearCuenta.alpha = 0.6f
        } else {
            btnCrearCuenta.alpha = 1.0f
        }
    }
    // Cuenta creada correctamente
    override fun cuentaCreada(mensaje: String) {
        mostrarMensaje(mensaje)
        volverAlLogin()
    }
    // Regresar al Login
    private fun volverAlLogin() {
        // Si el Login ya está abierto
        if (intent.getBooleanExtra("desde_login", false)) {
            finish()
            return
        }
        // Si viene directamente desde Inicio
        val siguiente = Intent(this, login::class.java)
        siguiente.putExtra("desde_carrito", intent.getBooleanExtra("desde_carrito", false))
        startActivity(siguiente)
        finish()
    }
    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.liberar()
        }
        super.onDestroy()
    }
}