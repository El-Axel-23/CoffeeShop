
package com.example.coffeshop.Vista

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.example.coffeshop.R
import com.example.coffeshop.Presentador.CrearCuentaPresentador

class crear_cuenta : AppCompatActivity(), CrearCuentaPresentador.VistaCrearCuenta, View.OnClickListener {
    private lateinit var presentador: CrearCuentaPresentador
    // CAMPOS
    private lateinit var txtNombreCompleto: EditText
    private lateinit var txtCorreoRegistro: EditText
    private lateinit var txtContrasenaRegistro: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var chkTerminos: CheckBox
    private lateinit var btnCrearCuenta: View
    private lateinit var btnVerContrasenaRegistro: ImageButton
    private lateinit var btnVerConfirmar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        presentador = CrearCuentaPresentador(this)
        txtNombreCompleto = findViewById(R.id.txtNombreCompleto)
        txtCorreoRegistro = findViewById(R.id.txtCorreoRegistro)
        txtContrasenaRegistro = findViewById(R.id.txtContrasenaRegistro)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        chkTerminos = findViewById(R.id.chkTerminos)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)
        btnVerContrasenaRegistro = findViewById(R.id.btnVerContrasenaRegistro)
        btnVerConfirmar = findViewById(R.id.btnVerConfirmar)
        txtContrasenaRegistro.transformationMethod = PasswordTransformationMethod.getInstance()
        txtConfirmarContrasena.transformationMethod = PasswordTransformationMethod.getInstance()
        btnVerContrasenaRegistro.setImageResource(R.drawable.ic_ojo_cerrado)
        btnVerConfirmar.setImageResource(R.drawable.ic_ojo_cerrado)
        btnVerContrasenaRegistro.contentDescription = "Mostrar contraseña"
        btnVerConfirmar.contentDescription = "Mostrar contraseña"
        btnCrearCuenta.setOnClickListener(this)
        btnVerContrasenaRegistro.setOnClickListener(this)
        btnVerConfirmar.setOnClickListener(this)

        findViewById<View>(R.id.btnRegresar).setOnClickListener(this)
        findViewById<View>(R.id.txtIrLogin).setOnClickListener(this)
        findViewById<View>(R.id.btnRegistroGoogle).setOnClickListener(this)
        findViewById<View>(R.id.btnRegistroApple).setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            // CREAR CUENTA
            R.id.btnCrearCuenta -> {
                presentador.registrar(
                    txtNombreCompleto.text.toString(),
                    txtCorreoRegistro.text.toString(),
                    txtContrasenaRegistro.text.toString(),
                    txtConfirmarContrasena.text.toString(),
                    chkTerminos.isChecked
                )
            }
            // OJO DE CONTRASEÑA
            R.id.btnVerContrasenaRegistro -> {
                cambiarVisibilidad(
                    txtContrasenaRegistro,
                    btnVerContrasenaRegistro
                )
            }
            // OJO DE CONFIRMACION
            R.id.btnVerConfirmar -> {
                cambiarVisibilidad(
                    txtConfirmarContrasena,
                    btnVerConfirmar
                )
            }
            // REGRESAR
            R.id.btnRegresar -> {
                finish()
            }
            // IR AL LOGIN
            R.id.txtIrLogin -> {
                volverAlLogin()
            }
            // REGISTRO CON GOOGLE
            R.id.btnRegistroGoogle -> {
                mostrarMensaje(
                    "Registro con Google aún no configurado"
                )
            }
            // REGISTRO CON APPLE
            R.id.btnRegistroApple -> {
                mostrarMensaje(
                    "Registro con Apple aún no configurado"
                )
            }
        }
    }
    // MOSTRAR Y OCULTAR CONTRASEÑAS
    private fun cambiarVisibilidad(
        campo: EditText,
        boton: ImageButton
    ) {
        if (campo.transformationMethod is PasswordTransformationMethod) {
            // MOSTRAR CONTRASEÑA
            campo.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            boton.setImageResource(
                R.drawable.ic_ojo
            )
            boton.contentDescription =
                "Ocultar contraseña"

        } else {
            // OCULTAR CONTRASEÑA
            campo.transformationMethod =
                PasswordTransformationMethod.getInstance()
            boton.setImageResource(
                R.drawable.ic_ojo_cerrado
            )
            boton.contentDescription =
                "Mostrar contraseña"
        }
        // MANTENER CURSOR AL FINAL
        campo.setSelection(
            campo.text.length
        )
    }
    // VALIDACIONES VISUALES
    override fun mostrarErrorNombre(mensaje: String) {
        txtNombreCompleto.error = mensaje
        txtNombreCompleto.requestFocus()
    }
    override fun mostrarErrorCorreo(mensaje: String) {
        txtCorreoRegistro.error = mensaje
        txtCorreoRegistro.requestFocus()
    }
    override fun mostrarErrorContrasena(mensaje: String) {
        txtContrasenaRegistro.error = mensaje
        txtContrasenaRegistro.requestFocus()
    }
    override fun mostrarErrorConfirmacion(mensaje: String) {
        txtConfirmarContrasena.error = mensaje
        txtConfirmarContrasena.requestFocus()
    }
    override fun mostrarErrorTerminos(mensaje: String) {
        mostrarMensaje(mensaje)
    }
    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
    override fun mostrarCargando(cargando: Boolean) {
        btnCrearCuenta.isEnabled = !cargando
        if (cargando) {
            btnCrearCuenta.alpha = 0.6f
        } else {
            btnCrearCuenta.alpha = 1.0f
        }
    }
    // CUENTA CREADA CORRECTAMENTE
    override fun cuentaCreada(mensaje: String) {
        mostrarMensaje(mensaje)
        volverAlLogin()
    }
    // REGRESAR AL LOGIN
    private fun volverAlLogin() {
        // SI EL LOGIN YA ESTA ABIERTO DETRAS
        if (intent.getBooleanExtra("desde_login", false)) {
            finish()
            return
        }
        // SI VIENE DIRECTAMENTE DESDE INICIO
        val siguiente = Intent(
            this,
            login::class.java
        )
        siguiente.putExtra(
            "desde_carrito",
            intent.getBooleanExtra(
                "desde_carrito",
                false
            )
        )
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